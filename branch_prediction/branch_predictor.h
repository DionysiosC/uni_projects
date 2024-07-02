#ifndef BRANCH_PREDICTOR_H
#define BRANCH_PREDICTOR_H

#include <sstream> // std::ostringstream
#include <cmath>   // pow()
#include <cstring> // memset()!
#include "ras.h"

// Add any includes here....
#include <assert.h>     /* assert */
#include <list>

typedef std::list<ADDRINT>::iterator set_iterator_t;


/**
 * A generic BranchPredictor base class.
 * All predictors can be subclasses with overloaded predict(), update(), getNameAndConfig(), getResults()
 * methods.
 **/
class BranchPredictor
{
public:
    BranchPredictor() : correct_predictions(0), incorrect_predictions(0) {};
    ~BranchPredictor() {};

    virtual bool predict(ADDRINT ip, ADDRINT target) = 0;  // target is needed only for some static predictors
    virtual void update(bool predicted, bool actual, ADDRINT ip, ADDRINT target, bool isCall=false, bool isRet=false) = 0;
    virtual std::string getNameAndConfig() = 0;
    virtual std::string getResults() = 0;


    void resetCounters() { correct_predictions = incorrect_predictions = 0; };

protected:
    void updateCounters(bool predicted, bool actual) {
        if (predicted == actual)
            correct_predictions++;
        else
            incorrect_predictions++;
    };

    UINT64 correct_predictions;
    UINT64 incorrect_predictions;
};

// ------------------------------------------------------
// NbitPredictor - a generalization of 1,2 bit saturating counters
// ------------------------------------------------------
class NbitPredictor : public BranchPredictor
{
public:
    NbitPredictor(unsigned index_bits_, unsigned cntr_bits_)
        : BranchPredictor(), index_bits(index_bits_), cntr_bits(cntr_bits_) {
        table_entries = 1 << index_bits;
        TABLE = new unsigned int[table_entries];
        memset(TABLE, 0, table_entries * sizeof(*TABLE));
        
        COUNTER_MAX = (1 << cntr_bits) - 1;
        resetCounters();
    };
    ~NbitPredictor() { delete TABLE; };

    virtual bool predict(ADDRINT ip, ADDRINT target) {
        unsigned int ip_table_index = ip % table_entries;
        unsigned long long ip_table_value = TABLE[ip_table_index];
        unsigned long long prediction = ip_table_value >> (cntr_bits - 1);   // get the msb of the counter
        return (prediction != 0);
    };

    virtual void update(bool predicted, bool actual, ADDRINT ip, ADDRINT target, bool isCall=false, bool isRet=false) {
        unsigned int ip_table_index = ip % table_entries;
        if (actual) {
            if (TABLE[ip_table_index] < COUNTER_MAX)
                TABLE[ip_table_index]++;
        } else {
            if (TABLE[ip_table_index] > 0)
                TABLE[ip_table_index]--;
        }
        
        updateCounters(predicted, actual);
    };

    virtual std::string getNameAndConfig() {
        std::ostringstream stream;
        stream << "Nbit-" << pow(2.0,double(index_bits)) / 1024.0 << "K-" << cntr_bits;
        return stream.str();
    }

    virtual std::string getResults() {
        std::ostringstream stream;
        stream << "Correct: " << correct_predictions << " Incorrect: "<< incorrect_predictions;
        return stream.str();
    }

private:
    unsigned int index_bits, cntr_bits;
    unsigned int COUNTER_MAX;
    
    unsigned int *TABLE;
    unsigned int table_entries;
};

// Fill in the BTB implementation ...  Assume LRU replacement and full tags
class BTBPredictor : public BranchPredictor
{
public:
    BTBPredictor(int btb_lines, int btb_assoc, int ras_entries_)
        : table_lines(btb_lines), table_assoc(btb_assoc), ras_entries(ras_entries_)
    {
        ras = new RAS(ras_entries_);   // get a RAS
        num_sets = btb_lines/btb_assoc; // removed the 'int' specifier as we want to modify the private field of the class called 'num_sets'
		
        assert((num_sets & (num_sets-1)) == 0);  // number of sets must be power of 2
        TABLE = new std::list<ADDRINT>[num_sets];   // An array of lists.
        resetCounters();
        
		/* my code */
		for (int i = 0; i < num_sets; ++i) {
			TABLE[i] = std::list<ADDRINT>(btb_assoc, 0);
		}
		ras_misses = 0;
		btb_misses = 0;
		
    }

    ~BTBPredictor() {
        delete ras;
        delete [] TABLE;
    }

    virtual bool predict(ADDRINT ip , ADDRINT target) {
        
		/* my code */
		
		// Choose the correct set
		int set_index = ip % num_sets;
		std::list<ADDRINT>& target_list = TABLE[set_index];
		actual_target = target;
		
		// We check the specific set inside the TABLE for the target address
		for (ADDRINT addr : target_list) {
			if (addr == target) {
				return true;
			}
		}
        return false;
    }

	// LRU in the BTB: the most recently used branch is at the back and the least recently used is at the front
    virtual void update(bool predicted, bool actual, ADDRINT ip, ADDRINT target, bool isCall=false, bool isRet=false) {
        /* my code */
		// Now, if the instruction is conditional branch, we check if our prediction was correct and act accordingly
		// (we replace an address of the TABLE with respect to associativity)
		if (!isRet && !isCall){
			// Choose the correct set (the correct list of the TABLE array)
			int set_index = ip % num_sets;
			std::list<ADDRINT>& target_list = TABLE[set_index];
			
			if ((!actual) && predicted){ // prediction miss and not taken -> the branch must be removed
				
				target_list.remove(target);
				
			}else if(actual && (!predicted)){ // prediction miss and taken -> the branch must be added to the BTB
			
				// If the list is already full, remove the least used (the first) entry
				if (int(target_list.size()) >= table_assoc) {
					target_list.pop_front();
				}
				
				
				// Add the new target address to the list
				target_list.push_back(target);
			}
			else if (actual && predicted){ // prediction was correct and taken (BTB is only for the branches that are predicted to be taken)
				
				// It's LRU, so the most recent branch must be at the back
				target_list.remove(target);					
				target_list.push_back(target);
				
			}
			updateCounters(predicted, actual);
			
		}else if (isCall){ // if the instruction is a call statement, we simply push to the RAS the return address ie the instruction pointer
			ras->push_addr(ip);
		}	
		// If the instruction is a return statement, we use the RAS
		else{
			bool result = ras->pop_addr_and_check(target);	
			if (!result)
				ras_misses++;
			updateCounters(result, actual); // we use 'result' instead of 'predicted' as if we did our prediction much earlier using RAS
		}
		
    }

    virtual std::string getNameAndConfig() { 
        std::ostringstream stream;
        stream << "BTB-" << table_lines << "-" << table_assoc << "-" << ras_entries;
        return stream.str();
    }
	
    virtual std::string getResults() {
        std::ostringstream stream;
        stream << "Correct: " << correct_predictions << " Incorrect: "<< incorrect_predictions;
		stream << " --> BTB misses: "<<btb_misses<<" RAS misses: "<< ras_misses;
        return stream.str();
    }
 
private:
    int table_lines, table_assoc;
    int num_sets;
    int ras_entries;
    RAS *ras;   // the RAS stack
    std::list<ADDRINT> *TABLE;   // An array of lists.
    /* Write your code here */
	int ras_misses;
	int btb_misses;
	ADDRINT actual_target;
	
};

class BTFNTPredictor: public BranchPredictor
{
public:
	BTFNTPredictor (){}

	~BTFNTPredictor(){}
		
    virtual bool predict(ADDRINT ip , ADDRINT target) {
		if (target<ip){
			return true;
		}return false;
	}
	
	virtual void update(bool predicted, bool actual, ADDRINT ip, ADDRINT target, bool isCall=false, bool isRet=false) {
		updateCounters(predicted, actual);
	}
	
	virtual std::string getNameAndConfig() { 
		std::ostringstream stream;
        stream << "BTFNT";
        return stream.str();
	}
	
	virtual std::string getResults() {
        std::ostringstream stream;
        stream << "Correct: " << correct_predictions << " Incorrect: "<< incorrect_predictions;
        
        return stream.str();		
	}	
	

private:

	
};

class StaticNotTakenPredictor: public BranchPredictor
{
public:
	StaticNotTakenPredictor(){
		
	}
	
	~StaticNotTakenPredictor(){
		
	}

	virtual bool predict(ADDRINT ip , ADDRINT target) {
		return false;
	}
	
	virtual void update(bool predicted, bool actual, ADDRINT ip, ADDRINT target, bool isCall=false, bool isRet=false) {
		updateCounters(predicted, actual);
	}
	
	virtual std::string getNameAndConfig() { 
		std::ostringstream stream;
        stream << "StaticNotTaken";
        return stream.str();
	}
	
	virtual std::string getResults() {
        std::ostringstream stream;
        stream << "Correct: " << correct_predictions << " Incorrect: "<< incorrect_predictions << "\n";
        
        return stream.str();		
	}	
	
private:
	
};

class GSharePredictor: public BranchPredictor // With global history and 2bit PHT, 8K places
{
public:
	GSharePredictor(){
		counter = 0;
		history_reg = 0;
		
		pht = new int* [pht_places];
		for (int i=0;i<pht_places; ++i){
			pht[i] = new int[2];
			pht[i][0] = 0;
			pht[i][1] = 0;
				
		}
		
	}

	~GSharePredictor(){
		delete [] pht;
		
	}
	
	virtual bool predict(ADDRINT ip , ADDRINT target) {
		int mask = 0x1FFF;
		pht_pos = (ip & mask) ^ (history_reg & mask);
		if ((pht[pht_pos][0] == 1 and pht[pht_pos][1] == 1) or (pht[pht_pos][0] == 1 and pht[pht_pos][1] == 0))
			return true;
		return false;
	}
	
	virtual void update(bool predicted, bool actual, ADDRINT ip, ADDRINT target, bool isCall=false, bool isRet=false) {
		if (counter>global_history_bits){
			counter=0;
		}	
		
		int temp;
		if (actual){
			
			// PHT Update
			if (predicted){
				pht[pht_pos][0]= 1;
				pht[pht_pos][1]= 1;
			}else{
				if (pht[pht_pos][1] == 0){
					pht[pht_pos][0]= 0;
					pht[pht_pos][1]= 0;
				}else{
					pht[pht_pos][0]= 0;
					pht[pht_pos][1]= 1;
				}				
			}
			
			temp = (1<<counter)-1;
			history_reg &= temp;
		}else{
			
			// PHT Update
			if (predicted){
				if (pht[pht_pos][1]==1){
					pht[pht_pos][0]= 1;
					pht[pht_pos][1]= 0;
				}else{
					pht[pht_pos][0]= 0;
					pht[pht_pos][1]= 0;					
				}
			}else{
				if (pht[pht_pos][0]==1){
					pht[pht_pos][0]= 0;
					pht[pht_pos][1]= 0;
				}else{
					pht[pht_pos][0]= 0;
					pht[pht_pos][1]= 1;
			}
			
			temp = (0<<counter)-1;
			history_reg &= temp;
			
			}
		}
		counter++;
		updateCounters(predicted, actual);
	}
	
	virtual std::string getNameAndConfig() { 
		std::ostringstream stream;
        stream << "GShare";
        return stream.str();
	}
	
	virtual std::string getResults() {
        std::ostringstream stream;
        stream << "Correct: " << correct_predictions << " Incorrect: "<< incorrect_predictions << "\n";        
        return stream.str();		
	}	
	
private:
	const int global_history_bits = 13;
	const int pht_witdth = 2;
	const int pht_places = 8000;
	
	int pht_pos;
	int counter;
	int history_reg;
	int **pht;
	
	
};


#endif
