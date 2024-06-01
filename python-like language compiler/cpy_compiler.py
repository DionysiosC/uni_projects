# In order to run this compiler, you must use this syntax in the windows cmd: python3 cpy_compiler.py <your_program>.cpy
# Now, after running the compiler, three files must have been created: the intermediate code (.int) file,the symbol table file (.sym)
#  and the RISC-V assembly code file (.asm). In order to always create a new distinct file every time this program is run,
#  the current date and time is concatcated to the name of each file.

# Libraries
import sys
import os
import datetime

# Global variables
reserved_words = ['main', 'def', '#def', '#int', 'global','if', 'elif', 'else', 'while', 'print', 'return','input', 'int', 'and', 'or', 'not']
#token_family = ['identifier', 'number', 'keyword', 'arithmOperator', 'relOperator', 'assignment', 'delimiter', 'groupSymbols', 'comments']
currentLineNumber=0

##################################### Lexer ##################################### 
class Token:
    def __init__(self, recognized_string, family, line):
        self.recognized_string=recognized_string
        self.family = family
        self.line = line
        
    def __str__(self):
        return self.recognized_string + " :: family: " + self.family + " line: " + str(self.line)

### lexical analyzer ###
class lexer:
    programLines = []
    currentLineIndex=0

    def __init__(self, current_line, file_path):
        self.current_line = current_line
        self.file_path = file_path

        if not(file_path.endswith('.cpy')):
            print('File type error - Not a ".cpy" file')
            exit()
            
        try:
            f = open(file_path, "r")
        
            self.programLines = [line.rstrip() for line in f]
        
            f.close()
        
        except:
            print('File not found error in lexer analysis phase. Exiting...')
            exit()
        
        print("File opened and read")
        
        #print(self.programLines)
    
    def __error(self, occasion):
        print("Lexer error spotted at line no."+str(currentLineNumber+1))
        if (occasion=='number'):
            print("Illegal number constant, cannot include letters and the range should be -32767 to 32767")
        elif (occasion=='hashtag'):
            print("Illegal expression after hashtag character.")
        elif (occasion == 'limits'):
            print("Illegal length of variable name")
        elif (occasion == 'division'):
            print("Illegal '/' character. Must be '//'")
        elif (occasion == 'unknown'):
            print('Unknown character error')
        exit()
    
    def next_token(self):
        global currentLineNumber
        
            
        currentLine =''
        currentString = ''
        family = ''
        
        lineLength = len(self.programLines[currentLineNumber])
        while lineLength==0 or self.currentLineIndex >= lineLength:
            self.currentLineIndex=0
            currentLineNumber+=1
            
            try:
                lineLength = len(self.programLines[currentLineNumber])
            except: # except for when we reach the end of file
                return Token('EOF', "endOfFile", currentLineNumber)
            
        currentLine = [line.rstrip() for line in self.programLines[currentLineNumber]]
        #print(lineLength)
        #print(currentLine)
        #print(self.currentLineIndex)
        
        counter = 30
        while(True):
            # check if whitespace
            if (currentLine[self.currentLineIndex]==''):
                self.currentLineIndex+=1
            
            # id or keyword
            elif (currentLine[self.currentLineIndex].isalpha()):
                
                while (currentLine[self.currentLineIndex].isalpha() or currentLine[self.currentLineIndex].isdigit()):
                    #print('Current char read: '+currentLine[self.currentLineIndex])
                    currentString+=currentLine[self.currentLineIndex]
                    self.currentLineIndex+=1                   
                    counter-=1
                    if counter<0:
                        self.__error('limits')
                    if self.currentLineIndex>=lineLength:
                        break
                    #print(currentString)
                
                
                if (currentString in reserved_words):
                    family = 'keyword'                       
                    break
                else:
                    family = 'id'
                    break
                        
            # number
            elif (currentLine[self.currentLineIndex].isdigit()):
                while (currentLine[self.currentLineIndex].isdigit()):
                    currentString+=currentLine[self.currentLineIndex]
                    self.currentLineIndex+=1
                    if self.currentLineIndex>=lineLength:
                        break 
                    if currentLine[self.currentLineIndex].isalpha():
                        self.__error('number')
                    
                if (int(currentString)>32767 or int(currentString)<-32767):
                    self.__error('number')
                family = 'number'
                break
            
            # grouping symbol
            elif (currentLine[self.currentLineIndex] == ')' or currentLine[self.currentLineIndex] == '('):
                currentString+=currentLine[self.currentLineIndex]
                family='groupSymbol'
                self.currentLineIndex+=1
                break
            
            #delimiter
            elif (currentLine[self.currentLineIndex]==':' or currentLine[self.currentLineIndex]==','):
                currentString+=currentLine[self.currentLineIndex]
                family = 'delimiter'
                self.currentLineIndex+=1
                break
            
            #arithmetic operators
            elif (currentLine[self.currentLineIndex] == '*' or currentLine[self.currentLineIndex] == '+' or currentLine[self.currentLineIndex]=='-' or currentLine[self.currentLineIndex]=='%'):
                currentString+=currentLine[self.currentLineIndex]
                family = 'arithmOperator'
                self.currentLineIndex+=1
                break
            
            # arithmetic operators -> division
            elif (currentLine[self.currentLineIndex] == '/'):
                currentString+=currentLine[self.currentLineIndex]
                self.currentLineIndex+=1
                if (currentLine[self.currentLineIndex] == '/'):
                    currentString+=currentLine[self.currentLineIndex]
                    self.currentLineIndex+=1
                    family = 'arithmOperator'
                    break
                else:
                    self.__error('division')
                    
            #overlapping families
            # assignment operator (or relational operator)
            elif (currentLine[self.currentLineIndex] == '='):
                currentString+=currentLine[self.currentLineIndex]
                if (self.currentLineIndex<lineLength-1):
                    self.currentLineIndex+=1
                    if (currentLine[self.currentLineIndex] == '='):
                        currentString+=currentLine[self.currentLineIndex]
                        self.currentLineIndex+=1
                        family='relOperator'
                        break
                
                family='assignment'
                break
                
            # relational Operators
            elif (currentLine[self.currentLineIndex] == '>' or currentLine[self.currentLineIndex] == '<' or currentLine[self.currentLineIndex] == '!'):
                currentString+=currentLine[self.currentLineIndex]
                self.currentLineIndex+=1
                if (currentLine[self.currentLineIndex] == '='):
                    currentString+=currentLine[self.currentLineIndex]
                    self.currentLineIndex+=1
                
                    
                family='relOperator'
                break
            
            # comments or the '#{' or '#}' group symbol or the '#int' keyword
            elif (currentLine[self.currentLineIndex] == '#'):
                currentString+=currentLine[self.currentLineIndex]
                if self.currentLineIndex<lineLength-1:
                    self.currentLineIndex+=1
                else:
                    self.__error('hashtag')
                
                if (currentLine[self.currentLineIndex] == '#'):
                    currentString+=currentLine[self.currentLineIndex]
                    self.currentLineIndex+=1
                    family = 'comments'
                    break
                
                elif (currentLine[self.currentLineIndex] == '}' or currentLine[self.currentLineIndex] == '{'):
                    currentString+=currentLine[self.currentLineIndex]
                    self.currentLineIndex+=1
                    family = 'groupSymbol'
                    break
                
                elif (currentLine[self.currentLineIndex] == 'i' or currentLine[self.currentLineIndex] == 'd'):
                    
                    while (currentLine[self.currentLineIndex].isalpha() and self.currentLineIndex<lineLength-1):
                        #print(currentLine)
                        currentString+=currentLine[self.currentLineIndex]
                        self.currentLineIndex+=1
                        
                    currentString+=currentLine[self.currentLineIndex]
                    if (currentString=='#int' or currentString=='#def'):
                        family = 'keyword'
                        self.currentLineIndex+=1
                        break    
                    else:
                        self.__error('hashtag')
                        
                else:
                    self.__error('hashtag')
            
            # unknown char
            else:
                self.__error('unknown')
                    
               
            
        nextToken = Token(currentString, family, currentLineNumber+1)
        return nextToken

##################################### Symbol Table code #####################################
class Entity:
    def __init__(self, name):
        self.name = name
        
    def __str__(self):
        return 'Entity: '+str(self.name)

class Constant(Entity):
    def __init__(self, name, datatype, value):
        super().__init__(name)
        self.datatype = datatype
        self.value = value
    
    def __str__(self):
        return 'Constant:'+str(self.name)+'/'+str(self.value)

class Variable(Entity):
    def __init__(self, name, datatype, offset):
        super().__init__(name)
        self.datatype = datatype
        self.offset = offset
        
    def __str__(self):
        return 'Variable:' +str(self.name)+'/'+str(self.offset)

class TempVariable(Variable):
    def __init__(self, name, datatype, offset):
        super().__init__(name, datatype, offset)
    
    def __str__(self):
        return 'TempVar:' +str(self.name)+'/'+str(self.offset) 
       

class Parameter(Entity):
    def __init__(self, name, parMode, offset):
        super().__init__(name)
        self.parMode = parMode
        self.offset = offset
    
    def __str__(self):
        return "Parameter: " + str(self.name)  + "/" + str(self.offset)
        
        
class Function(Entity):
    def __init__(self, name, startingQuad, formalParameters, framelength, datatype):
        super().__init__(name)                      
        self.startingQuad = startingQuad            
        self.formalParameters = formalParameters    
        self.framelength = framelength
        
        self.datatype = datatype # returning data type

    def __str__(self):        
        finalStr = 'Function: '+str(self.name) + "/" + str(self.startingQuad) + "/" + str(self.framelength)
        #print(self.formalParameters)
        for arg in self.formalParameters:
            finalStr += ' ' + str(arg)
        return finalStr
            
        
class Scope:
    def __init__(self, level):
        self.entity_list = [] # list of entities like variable obj, functions etc
        self.level = level
        self.currentOffset=12
    
    def __str__(self):
        finalString = '(size: '+str(self.currentOffset)+ ') Level: ' + str(self.level) + ' ---> '
        for entity in self.entity_list:
            finalString += str(entity) + ' -> '           
            
        finalString = finalString.rstrip(' -> ')  # Remove the trailing arrow
        
        return finalString 
    
    def getTotalScopeLength(self):
        return self.currentOffset+4

# for whenever we reach a declaration of a typical parameter of a function
# Does not act as a record in a Scope list (table)
class RecArgument:
    def __init__(self, parmode, argtype):
        self.parmode = parmode # cv for cutepy always
        self.argtype = argtype # int for cutepy always
        
    def __str__(self):
        return '$-Function RecParam:'+str(self.parmode)+'/'+str(self.argtype)+'-$'

class SymbolTable:
    def __init__(self):
        self.table = [] # list of scopes
        self.table_length = 0 # number of scopes
        self.timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S") # for the naming of the file
    
    def __str__(self):
        finalString = 'Current Symbol table:\n'
        for i in reversed(self.table):
            finalString += (str(i) +'\n')
        return finalString
    
    # Add a record at the rightmost Scope of the table
    def addRecord(self, record):
        if self.table_length>0:
            self.table[-1].entity_list.append(record)
            if not (isinstance(record, Constant) or isinstance(record, Function)):                
                self.table[-1].currentOffset += 4

    # Add record argument to the function at the current level (the function should be the latest entity of the scope and
    # this function is to be called just before the new level is created for the function in question)
    # This function is custom-made for the needs of the Cpy language that supports only int variables and call-by-value passing
    def addRecArgs(self, quantity):
        finalList = []
        
        for i in range(quantity):           
            newArg = RecArgument('cv', 'int')
            finalList.append(newArg)
            
        #print(finalList)
        return finalList
 
    def getLatestOffset(self):
        if self.table_length>0:
            return self.table[-1].currentOffset
        else:
            return 12
 
    # append a Scope to the end of the symbol table list
    def addLevel(self):
        self.table.append(Scope(self.table_length))
        self.table_length+=1
    
    # remove the rightmost scope (along with its records)
    def removeScope(self):
        if self.table_length>0:
            self.table_length-=1
            self.table.pop()
    
    # Search for an entity (if two entities share the same name, the first to be found is returned, the search starts from the rightmost scope)
    def getRecord(self, name):        
        if name == 'main':
            return [self.table[0], 0]
            
        for scope in reversed(self.table):
            for record in scope.entity_list:
                if record.name == name:
                    return [record, scope.level]
        
        print("Error: The variable or function named "+str(name)+" has not been defined. Exiting...")
        exit()
    
    def getLastFunctionLevel(self):
        if len(self.table) == 1:
            return 0
            
        for scope in reversed(self.table):
            for record in scope.entity_list:
                if isinstance(record, Function):
                    return scope.level
                
    
    
    # Update the fields of a function record, for when the related info becomes known
    def updateFields(self, proc_name, frame_length, starting_quad, formal_parameters):
        
        if len(self.table) > 1:
            records = self.table[-2].entity_list # goto the second scope from the last one and retrieve its records list
        else:
            records = self.table[-1].entity_list # goto the last element
        
        for r in records:
            if r.name == proc_name and (isinstance(r, Function)):
                r.framelength = frame_length
                r.startingQuad = starting_quad
                r.formalParameters = formal_parameters
                break
    
    # Update the symbol file with the latest records and scopes
    def symbolTableFileUpdate(self):
        try:
            
            symtablefile = open(self.timestamp+'_symbol_table.sym', 'a')
            symtablefile.write(str(self)+'\n')
            symtablefile.close()
            
        except Exception as e:
            print('Error while creating or updating symbol table file:', e)
            exit()
              

##################################### Intermediate code #####################################         

# Quad class used in intermediate code
class Quad:
    def __init__(self, label, op, arg1, arg2, target):
        self.label = label
        self.op = op
        self.arg1 = arg1
        self.arg2 = arg2
        self.target = target

       
    def __str__(self):
        return str(self.label) +':' + str(self.op) + ',' + str(self.arg1) + ',' + str(self.arg2) + ',' + str(self.target)
    
    def __eq__(self, anotherQuad):
        if (self.op == anotherQuad.op and 
            self.arg1 == anotherQuad.arg1 and 
            self.arg2 == anotherQuad.arg2 and 
            self.target == anotherQuad.target):
            return True
        return False
    
    def getQuadLabel(self):
        return self.label

# Quadlist class    
class QuadList:
    def __init__(self):
        self.program_list = [] # list with the quads
        self.quad_counter = 0 # number of quads (does not represent the length of the program list)
    
    def __str__(self):
        size = len(self.program_list)
        finalStr = ''
        for i in range(size):
            finalStr += str(self.program_list[i]) + '\n'
        return finalStr
    
    def genQuad(self, op, arg1, arg2, target):
    
        self.quad_counter+=1
        next_quad = Quad(self.quad_counter, op, arg1, arg2, target)
        
        self.program_list.append(next_quad)
        
        return next_quad

    
    def nextQuad(self):
        return self.quad_counter+1
    
    def getQuad(self, label):
        return self.program_list[label-1]
    
    def backpatch(self, ptrlist, new_label):
        for cur_quad in self.program_list:
            for label in ptrlist:
                if cur_quad.label == label:
                    cur_quad.target = new_label
    
    # function that creates the file with the intermediate code
    def WriteInterCodeFile(self):        
        try:
            timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
            interCodeFile = open(timestamp+'_intermediateCode.int', 'w')
            for item in self.program_list:
                #print(item)
                interCodeFile.write(str(item)+'\n')
            interCodeFile.close()
            
        except Exception as e:
            print("Error "+e+" Exiting...")
            
            return 0
        return 1

# Quad pointer list aka list of pointers to quads
class QuadPtrList:
    def __init__(self):
        self.labelList = []
        
    def __str__(self):
        finalStr = ''
        list_length = len(self.labelList)
        if (list_length==0):
            return 'Quad pointer list is empty'
            
        for i in range(list_length):
            finalStr+=self.labelList[i]+ ' '            
        return 'The list of labels of quad pointer list: '+finalStr
    
    def emptyList(self):
        self.labelList = []

    def makeList(self, label):
        self.labelList=[label]
        return self.labelList

    def mergeList(self, label_1, label_2):
        self.labelList = label_1 + label_2
        return self.labelList
           
    def getList(self):
        return self.labelList
    
    def getListLen(self):
        return len(self.labelList)
    
    def addToList(self, label):
        self.labelList.append(label)
       
##################################### Parsing ##################################### 
class Parser:
    def __init__(self, lexer):
        self.lexer = lexer
        
        self.cur_temp_number = 0 # current number of temp variables used by the compiler
                
        # Inits for inter code
        self.quadptrlist = QuadPtrList()
        self.quadlist = QuadList()
        
        # Inits for symbol table
        self.symboltable = SymbolTable()
        
        # Add level for main function
        self.symboltable.addLevel()
        
        self.finalcode = FinalCode(self.quadlist,  self.symboltable)
    
    def get_token(self):
        global token
        token = self.lexer.next_token()
        #print(token)
        if token.family == 'comments':
            token = self.lexer.next_token()
            while token.family != 'comments' and token.recognized_string != 'EOF':
                token = self.lexer.next_token()
            token = self.lexer.next_token()
            #print(token)
        return token
    
    def __error(self, occasion, tokenInQuestion):
        global token
        print('Parser error detected, line: '+str(token.line))
        if occasion=='function':
            print('Illegal syntax in function')
        elif occasion=='declaration':
            print('Illegal syntax in a declaration')
        elif occasion=='id_list':
            print('Illegal syntax in a function parameters declaration')
        elif occasion == 'factor':
            print('Illegal syntax in a factor element')
        elif (occasion == 'statement'):
            print("Illegal syntax in statement")
        elif (occasion == 'assignment'):
            print ('Illegal syntax in assignment')
        elif (occasion == 'idtail'):
            print ('Illegal syntax in id tail part of a factor element')
        elif (occasion == 'whileStat'):
            print ('Illegal syntax in while statement')
        elif (occasion == 'ifStat'):
            print ('Illegal syntax in if statement')
        elif (occasion == 'printStat'):
            print ('Illegal syntax in print statement. Check parentheses')
        elif (occasion == 'expression'):
            print ('Illegal syntax, expression might be missing')
        elif (occasion == 'parenthesis'):
            print ('\'#} \' missing ')
        else:
            print (occasion)
        print('String in question: '+tokenInQuestion.recognized_string)
        exit()
    
    def newTemp(self):
        self.cur_temp_number+=1
        new_var = "T_" + str(self.cur_temp_number)
        self.symboltable.addRecord(TempVariable(self.getLastTemp(), 'integer', self.symboltable.getLatestOffset()))
        return new_var
    
    def getLastTemp(self):
        return "T_" + str(self.cur_temp_number)

     
    def syntax_analyzer(self):
        global token
        token = self.get_token()
        self.start_rule()
        print('Compilation successfully completed')
        
        #print(self.quadlist)
        flag = self.quadlist.WriteInterCodeFile()
        if (flag==1):
            print('Intermediate code file ready')
        else:
            print('Intermediate code file not created. Exiting...')
            exit()
        
        return [self.quadlist, self.symboltable]
           
    # Rules #
    def start_rule(self):
        global token
        while token.recognized_string == '#int' or token.recognized_string == 'global':
            self.declaration()
        while token.recognized_string == "def":
            self.def_function()
        if token.recognized_string == "#def":
            self.mainDef()
    
    def declaration(self):
        global token
        if token.recognized_string == '#int' or token.recognized_string == 'global':
            token = self.get_token()
            #print('Declaration: '+str(token))
            if token.family == 'id':
            
                self.symboltable.addRecord(Variable(token.recognized_string, 'integer', self.symboltable.getLatestOffset()))
                
                token = self.get_token()
                while token.recognized_string == ',':
                    token = self.get_token()
                    if token.family == 'id':
                        
                        self.symboltable.addRecord(Variable(token.recognized_string, 'integer', self.symboltable.getLatestOffset()))
                        token = self.get_token()
                        
                    else:
                        self.__error('declaration', token)
                        
                if token.recognized_string == '#int' or token.recognized_string == 'global':
                    self.declaration()
            else:
                self.__error('declaration', token)
                
    def def_function(self):
        global token
        params = ''
        if token.recognized_string == "def":
            token = self.get_token()
            
            if token.family == "id":
                function_name = token.recognized_string                
                token = self.get_token()
                
                self.symboltable.addRecord(Function(function_name, -1, [], -1, 'integer'))               
                
                if token.recognized_string == '(':
                    token = self.get_token()
                    
                    params = self.id_list()
                    #print(function_name+'->'+str(len(params)))
                    args = self.symboltable.addRecArgs(len(params))
                    #print(args)
                    self.symboltable.addLevel()
                    
                    for i in params:
                        self.symboltable.addRecord(Parameter(i, 'cv', self.symboltable.getLatestOffset()))
                    
                    if token.recognized_string == ')':
                        token = self.get_token()
                        if token.recognized_string ==  ':':
                            token = self.get_token()
                            if token.recognized_string == '#{':
                                token = self.get_token()
                                
                                # function main part
                                while token.recognized_string == '#int':
                                    self.declaration()
                                
                                while token.recognized_string == "def":
                                    self.def_function()
                                    
                                while token.recognized_string == "global":
                                    self.declaration()
                                
                                self.finalcode.createLabel(function_name)
                                instr = MemAccessAssemblyLine('sw', 'ra', 0, 'sp')
                                self.finalcode.FinalCodeFileUpdate(instr)
                                
                                strtQuad = self.quadlist.genQuad("begin_block", function_name, "_", "_")
                                starting_quad = self.quadlist.nextQuad()
                                
                                self.statements()                                
                                                              
                                self.quadlist.genQuad("end_block", function_name, "_", "_")
                                self.symboltable.updateFields(function_name, self.symboltable.getLatestOffset(), strtQuad.getQuadLabel(), args)                                
                                self.symboltable.symbolTableFileUpdate()
                                
                                                              
                                self.finalcode.createFinalCodeForBlock(self.quadlist, self.symboltable, starting_quad)
                                                               
                                instr = MemAccessAssemblyLine('lw', 'ra', 0, 'sp')
                                self.finalcode.FinalCodeFileUpdate(instr)
                                instr = JumpAssemblyLine('jr', 'ra')
                                self.finalcode.FinalCodeFileUpdate(instr)
                                
                                self.symboltable.removeScope()
                                
                                if token.recognized_string == '#}':
                                    token = self.get_token()
                                    
                                else:
                                    self.__error('function', token)
                                    
                            else:
                                self.__error('function', token)
                        else:
                            self.__error('function', token)
                        
                    else:
                        self.__error('function', token)
                    
                else:
                    self.__error('function', token)
            else:
                self.__error('function', token)
        
    def expression(self):
        global token
        # optional sign term
        optSign = ''
        if token.recognized_string=='-':
            optSign = token.recognized_string 
            token = self.get_token()
            
        elif token.recognized_string=='+':
            token = self.get_token()
            
        #print('from expression:' +str(token))
        t_1_place = self.term()
        while token.recognized_string=='+' or token.recognized_string=='-' or token.recognized_string=='%':
            operator = token.recognized_string
        
            token = self.get_token()
            t_2_place = self.term()
            
            w = self.newTemp()
            
            
            self.quadlist.genQuad(operator, t_1_place, t_2_place, w)
            t_1_place = w
            
        return str(optSign) + str(t_1_place)
                
    def term(self):
        global token
        f_1_place = self.factor()
        while token.recognized_string=='*' or token.recognized_string=='//':
            operator = token.recognized_string
            
            token = self.get_token()
            f_2_place = self.factor()
            
            w = self.newTemp()
            self.quadlist.genQuad(operator, f_1_place, f_2_place, w)
            f_1_place = w
        return f_1_place
    
    def factor(self):
        global token        
        if token.family == 'number':
            f_place = token.recognized_string
            
            token = self.get_token()
            return f_place
            
        elif token.recognized_string == '(':
            token = self.get_token()
            e_place = self.expression()
            if token.recognized_string == ')':
                token = self.get_token()
                return e_place
                
            else:
                self.__error('factor', token)
            
        elif token.family == 'id':
            f_place = token.recognized_string
            token = self.get_token()
            
            temp = self.idtail()
            
            # parameters in symbol table
            
            if (temp !=''):
                self.quadlist.genQuad("call", f_place, "_", "_")
                return self.getLastTemp()
            
            return f_place
        else:
            self.__error('expression',token)
    
    def idtail(self):
        global token
        par_list = ''
        if token.recognized_string == '(':
            token = self.get_token()
            
            if token.recognized_string == ')':
                token = self.get_token()
                return par_list
                
            par_list = self.actual_par_list()
            self.quadlist.genQuad('par', par_list, 'ret', '_')
            
            if token.recognized_string == ')':
                token = self.get_token()
            else:
                self.__error('idtail', token)
                
        return par_list
        
    def actual_par_list(self):
        global token
        par_list = []
        
        p = self.expression()
                
        par_list.append(p)
        
        while token.recognized_string==',':
            token = self.get_token()
            p = self.expression()
            par_list.append(p)
        
        for p in par_list:
            self.quadlist.genQuad("par", p, "cv", "_")
            
        return self.newTemp()    
    
    def returnStat(self):
        global token
        if token.recognized_string=='return':
            token = self.get_token()
            e_place = self.expression()
            
        else:
            self.__error('function', token)
        return e_place
    
    def printStat(self):
        global token
        e_place=None
        if token.recognized_string=='print':
            token = self.get_token()
            if token.recognized_string=='(':
                token = self.get_token()

                e_place = self.expression()
                if token.recognized_string==')':
                    token = self.get_token()
                else:
                    self.__error('printStat', token)
            else:
                self.__error('printStat', token)
        return e_place
    
    def id_list(self):
        global token
        parameters = []
        if token.family == 'id':
            parameters.append(token.recognized_string)
            token = self.get_token()
            while token.recognized_string == ',':
                token = self.get_token()
                if token.family == 'id':
                    parameters.append(token.recognized_string)
                    token = self.get_token()
                else:
                    self.__error('id_list', token)
        
        return parameters

    def statement_or_block(self):
        global token
        if token.recognized_string == '#{':
            token = self.get_token()
            self.statements()
            if token.recognized_string == '#}':
                token = self.get_token()
            else:
                self.__error('parenthesis', token)
        else:    
            self.statement()
        
    def statements(self):
        while (token.recognized_string in ['if', 'while','print', 'return']) or (token.family in ['id', 'number']):
            self.statement()
    
    def statement(self):
        if token.recognized_string == 'if' or token.recognized_string == 'while':
            self.structured_statement()
        else:
            self.simple_statement() 

    def structured_statement(self):
        if token.recognized_string == 'if':
            self.ifStat()
        elif token.recognized_string == 'while':
            self.whileStat()

    def simple_statement(self):
        if token.recognized_string == 'print':
            src = self.printStat()
            self.quadlist.genQuad('out', src, '_', '_')
            
        elif token.recognized_string == 'return':
            target = self.returnStat()
            self.quadlist.genQuad('ret', '_', '_', target)
            
        elif token.family == 'id':
            target = token.recognized_string
            temp = self.assignmentStat()
            self.quadlist.genQuad('=', temp, '_', target)

    def assignmentStat(self):
        global token
        retval = None
        if token.family == 'id':
            token = self.get_token()
            if token.recognized_string == '=':
                token = self.get_token()
                if token.recognized_string == 'int':
                    token = self.get_token()
                    if token.recognized_string == '(':
                        token = self.get_token()
                        if token.recognized_string == 'input':
                            token = self.get_token()
                            if token.recognized_string == '(':
                                token = self.get_token()
                                if token.recognized_string == ')':
                                    token = self.get_token()
                                    if token.recognized_string == ')':
                                        token = self.get_token()
                                        retval = self.newTemp()
                                        self.quadlist.genQuad("in", retval, "_", "_")
                                    else:
                                        self.__error('assignment', token)
                                else:
                                    self.__error('assignment', token)
                            else:
                                self.__error('assignment', token)
                                
                        else:
                            self.__error('assignment', token)
                    else:
                        self.__error('assignment', token)
                else: 
                    retval = self.expression()
            else:
                self.__error('assignment', token)
        
            return retval

    def whileStat(self):
        global token
        condQuad = self.quadlist.nextQuad()
        
        if token.recognized_string == 'while':
            token = self.get_token()
            cond_arr = self.condition()
            cond_true = cond_arr[0]
            cond_false = cond_arr[1]
            if token.recognized_string == ':':
                token = self.get_token()
                self.quadlist.backpatch(cond_true, self.quadlist.nextQuad())
                
                self.statement_or_block()
                    
                self.quadlist.genQuad('jump', '_', '_', condQuad)
                self.quadlist.backpatch(cond_false, self.quadlist.nextQuad())
            else:
                self.__error('whileStat', token)
                   
    def ifStat(self):
        global token
        if token.recognized_string == 'if':
            token = self.get_token()
            
            cond_arr = self.condition()
            cond_true = cond_arr[0]
            cond_false = cond_arr[1]
            
            if token.recognized_string == ':':
                token = self.get_token()
                
                self.quadlist.backpatch(cond_true, self.quadlist.nextQuad()) 
                
                self.statement_or_block()
                
                if_list = self.quadptrlist.makeList(self.quadlist.nextQuad())
                self.quadlist.genQuad('jump','_','_','_') # jump to the statements after the last elif or else part
                self.quadlist.backpatch(cond_false, self.quadlist.nextQuad()) # to goto the next elif or else block
                
                while token.recognized_string == 'elif':
                        
                    if_list = self.elifStat(if_list)
                    
                
                self.elseStat()
                self.quadlist.backpatch(if_list, self.quadlist.nextQuad())
                
                
            else:
                self.__error('ifStat', token)
    
    def elifStat(self, if_list):
        global token
        if token.recognized_string == 'elif':
            
            token = self.get_token()
            
            cond_arr = self.condition()
            cond_true = cond_arr[0]
            cond_false = cond_arr[1]
             
            if token.recognized_string == ':':
            
                token = self.get_token()
                
                self.quadlist.backpatch(cond_true, self.quadlist.nextQuad()) 
                
                self.statement_or_block()
                
                if_list_2 = self.quadptrlist.makeList(self.quadlist.nextQuad())
                self.quadlist.genQuad('jump','_','_','_') # jump to the statements after the last elif or else part
                
                self.quadlist.backpatch(cond_false, self.quadlist.nextQuad()) # to goto the next elif or else block
                
                if_list = self.quadptrlist.mergeList(if_list, if_list_2)
                
            else:
                self.__error('ifStat', token)
        return if_list

    def elseStat(self):
        global token
        #print('from else: '+str(token))
        if token.recognized_string == 'else':
            token = self.get_token()
            if token.recognized_string == ':':
                token = self.get_token()
                self.statement_or_block()
                
            else:
                self.__error('ifStat', token)                    
    
    def condition(self):
        global token
        q1_arr = self.boolTerm()
        b_true = q1_arr[0]
        b_false = q1_arr[1]
        
        while token.recognized_string=='or':
        
            token = self.get_token()
            self.quadlist.backpatch(b_false, self.quadlist.nextQuad())
            
            q2_arr = self.boolTerm()
            q2_true = q2_arr[0]
            q2_false = q2_arr[1]
            
            b_true = self.quadptrlist.mergeList(b_true, q2_true)
            b_false = q2_false
            
        return [b_true, b_false]
    
    def boolTerm(self):
        global token
        r1_arr = self.boolFactor()
        q_true = r1_arr[0]
        q_false = r1_arr[1]
        
        while token.recognized_string=='and':
            
            token = self.get_token()
            self.quadlist.backpatch(q_true, self.quadlist.nextQuad())
            
            r2_arr = self.boolFactor()
            r2_true = r2_arr[0]
            r2_false = r2_arr[1]
            q_false = self.quadptrlist.mergeList(q_false, r2_false)
            q_true = r2_true
            
        return [q_true, q_false]
    
    def boolFactor(self):
        global token
        
        r_true = []
        r_false = []
        
        if token.recognized_string == 'not':
            token = self.get_token()
            b_arr = self.condition()
            
            r_true = b_arr[1]
            r_false = b_arr[0]
        else:
            if (token.recognized_string == '('):
                token = self.get_token()
            
            e1_place = self.expression()
            
            
            #print('From boolfactor:'+str(token))
            
            if token.recognized_string in ['==', '!=', '<', '>', '<=', '>=']:
                rel_op = token.recognized_string
                token = self.get_token()
                #print('next From boolfactor: '+str(token))
                e2_place = self.expression()
                
                r_true = self.quadptrlist.makeList(self.quadlist.nextQuad())
                self.quadlist.genQuad(rel_op,e1_place, e2_place,'_')
                r_false = self.quadptrlist.makeList(self.quadlist.nextQuad())
                self.quadlist.genQuad('jump','_','_','_')
            else:
                self.__error('Error in bool factor.', token)
                
        return [r_true, r_false]
                
    def mainDef(self):
        global token
        if token.recognized_string == '#def':
            token = self.get_token()
            if token.recognized_string == 'main':
                #self.symboltable.addLevel()
                
                self.finalcode.FinalCodeFileUpdate('Lmain:')
                instr = ArithmAssemblyLine('addi', 'sp', 'sp', '^') # the framelength becomes known later
                line_number = self.finalcode.getCurrentLine()
                #print(line_number)
                self.finalcode.FinalCodeFileUpdate(instr)
                
                instr = MoveAssemblyLine('gp', 'sp')
                self.finalcode.FinalCodeFileUpdate(instr)
                
                starting_quad = self.quadlist.nextQuad()
                self.quadlist.genQuad('begin_block', 'main', "_", "_")
                
                token = self.get_token()
                
                self.declaration()
                self.statements()
                
                self.quadlist.genQuad('halt','_','_','_')
                self.quadlist.genQuad('end_block', 'main', "_", "_")          
                
                self.symboltable.symbolTableFileUpdate()
                
                finalFrameLen = self.symboltable.table[-1].currentOffset
                self.finalcode.replace_hat_in_line(line_number, finalFrameLen)
                self.finalcode.createFinalCodeForBlock(self.quadlist, self.symboltable, starting_quad)
                
                
                self.symboltable.removeScope()
                
                instr = AssignAssemblyLine('a0', '0')
                self.finalcode.FinalCodeFileUpdate(instr)
                instr = AssignAssemblyLine('a7', '93')
                self.finalcode.FinalCodeFileUpdate(instr)
                self.finalcode.FinalCodeFileUpdate('\tecall')
                
######################################### Final #########################################
class MemAccessAssemblyLine:
    def __init__(self, op, tar, off, base):
        self.op = op # either lw or sw
        self.tar = tar
        self.off = off # an integer (cannot be null for 0)
        self.base = base # base register
    
    def __str__(self):
        return '    '+str(self.op) + ' ' + str(self.tar) + ', ' + str(self.off) + '(' + str(self.base) + ')'        

class ArithmAssemblyLine:
    def __init__(self, op, tar, arg1, arg2):
        self.op = op # either add, addi, sub, subi, mul, div, rem
        self.tar = tar
        self.arg1 = arg1
        self.arg2 = arg2 # if op = addi or subi, then arg2 is an integer
    
    def __str__(self):
        return '    '+str(self.op) + ' ' + str(self.tar) + ', ' + str(self.arg1) + ', ' + str(self.arg2) 

class BranchAssemblyLine:
    def __init__(self, op, arg1, arg2, label):
        self.op = op # either beq, blt, bgt, ble, bge, bne
        self.arg1 = arg1
        self.arg2 = arg2
        self.label = label # the label target on the program
    
    def __str__(self):
        return '    '+str(self.op) + ' ' + str(self.arg1) + ', ' + str(self.arg2) + ', ' + str(self.label)

class JumpAssemblyLine:
    def __init__(self, op, tar):
        self.op = op # either j, jal, jr
        self.tar = tar # either a label when op = j, function name when op = jal or a the address saved in a register when op = jr
    
    def __str__(self):
        return '    '+str(self.op) + ' ' + str(self.tar)

class MoveAssemblyLine:
    def __init__(self, arg1, arg2):
        self.arg1 = arg1
        self.arg2 = arg2
    
    def __str__(self):
        return '    '+'mv '+str(self.arg1)+', '+str(self.arg2)

class AssignAssemblyLine:
    def __init__(self, reg,  value):
        self.reg = reg
        self.value = value
        
    def __str__(self):
        return '    '+'li '+str(self.reg)+', '+str(self.value)
    

class FinalCode:
    # getRecord is used here that returns: [entity, level]

    def __init__(self, intCode, symTable):
        self.timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S") # for the naming of the file
        self.currentLine = 1
        
        self.intCode = intCode
        self.symTable = symTable
        
        self.FinalCodeFileUpdate('.data')
        self.FinalCodeFileUpdate('str_nl: .asciz "\\n"')
                
        self.FinalCodeFileUpdate('.text')
        self.createLabel('0')
        instr = JumpAssemblyLine('j', 'Lmain')
        self.FinalCodeFileUpdate(str(instr))        
        #self.FinalCodeFileUpdate('L1:')

    def getCurrentLine(self):
        return self.currentLine
    
    def replace_hat_in_line(self, line_number, arg):
        with open(self.timestamp+'_final.asm', 'r') as file:
            lines = file.readlines()

        
        if line_number < 1 or line_number > len(lines):
            raise IndexError("Line number out of range")

        
        lines[line_number - 1] = lines[line_number - 1].replace('^', str(arg))

        
        with open(self.timestamp+'_final.asm', 'w') as file:
            file.writelines(lines)

        
    def gnvlcode(self, v):
        rec = self.symTable.getRecord(str(v))
        lvl = rec[1]
        instr = MemAccessAssemblyLine('lw', 't0', -4, 'sp')
        self.FinalCodeFileUpdate(str(instr))
        for i in range(lvl,0,-1):
            instr = MemAccessAssemblyLine('lw', 't0', -4, 't0')
            self.FinalCodeFileUpdate(str(instr))
            
        instr = ArithmAssemblyLine('addi', 't0', 't0', '-'+str(rec[0].offset))
        self.FinalCodeFileUpdate(str(instr))
        
    def loadvr(self, v, r):
        # No constants in this language (like "const a = 5") or passing by reference
        # Cases: global variable or local/temp variable 
        # v = source (or operand), r = reg = target
        
        # If v string represents an integer
        if self.is_integer(v):
            instr = AssignAssemblyLine(str(r), str(v))
            self.FinalCodeFileUpdate(instr)
        
        # Global
        elif self.symTable.getRecord(str(v))[1] == 0:
            rec = self.symTable.getRecord(str(v))
            lvl = rec[1]
            
            instr = MemAccessAssemblyLine('lw', str(r), '-'+str(rec[0].offset), 'gp')
            self.FinalCodeFileUpdate(str(instr))
            
        # Local variable or passed by value as parameter (still seen for the first time in the program)
        elif self.symTable.getRecord(str(v))[1] - self.symTable.getLastFunctionLevel() == 1:
            rec = self.symTable.getRecord(str(v))
            lvl = rec[1]
            
            instr = MemAccessAssemblyLine('lw', str(r), '-'+str(rec[0].offset), 'sp')
            self.FinalCodeFileUpdate(str(instr))
        
        # the situation when v has been declared in an ancestor function and passes by value to the current
        else:
            self.gnvlcode(v)
            rec = self.symTable.getRecord(str(v))
            lvl = rec[1]
            
            instr = MemAccessAssemblyLine('lw', str(r), 0, 't0')
            self.FinalCodeFileUpdate(str(instr))
            
        
        
    def storevr(self, v, r):
        # No constants in this language (like "const a = 5") or passing by reference
        # v = source (or operand), r = reg = target
        rec = self.symTable.getRecord(str(v))
        lvl = rec[1]
        
        # Global
        if lvl == 0:
            instr = MemAccessAssemblyLine('sw', str(r), '-'+str(rec[0].offset), 'gp')
            self.FinalCodeFileUpdate(str(instr))
            
        # Local variable or passed by value as parameter (still seen for the first time in the program)
        elif self.symTable.getRecord(str(v))[1] - self.symTable.getLastFunctionLevel() == 1:
            instr = MemAccessAssemblyLine('sw', str(r), '-'+str(rec[0].offset), 'sp')
            self.FinalCodeFileUpdate(str(instr))
        
        # the situation when v has been declared in an ancestor function and passes by value to the current
        else:
            self.gnvlcode(v)
            instr = MemAccessAssemblyLine('sw', str(r), 0, 't0')
            self.FinalCodeFileUpdate(str(instr))        

    def createLabel(self, arg):
        newL = 'L_'+ str(arg)+':'
        self.FinalCodeFileUpdate(newL)
    
    def createFinalCodeForBlock(self, quadlist, symtable, start_quad_label):
        # This function is called in the rule that parses a function (i.e. the def_function of defMain) right before the end_block
        # In the quadlist, it finds the begin_block quad and end_block quad of the function in question and starts to 
        # transform each quad into assembly code
        # It also uses the current information of the symbol table, to calculate the offsets and the addresses
        currentLabel = start_quad_label
        currentQuad = quadlist.getQuad(currentLabel)
        currentFuncName = currentQuad.arg1
        
        while (currentQuad.op != 'end_block'):
            #print(currentQuad)
            self.createLabel(currentLabel)
            
            operator = currentQuad.op
            arg1 = currentQuad.arg1
            arg2 = currentQuad.arg2
            target = currentQuad.target
            instr = ''      
            
            if (operator in [ '+', '-', '//', '%', '*' ]):
                
                if operator == '+':    
                    if self.is_integer(arg1) and not(self.is_integer(arg2)):
                        self.loadvr(arg2,'t0')
                        instr = ArithmAssemblyLine('addi', 't0', 't0', arg1)
                        self.FinalCodeFileUpdate(instr)
                    
                    elif self.is_integer(arg2) and not(self.is_integer(arg1)):
                        self.loadvr(arg1,'t0')
                        instr = ArithmAssemblyLine('addi', 't0', 't0', arg2)
                        self.FinalCodeFileUpdate(instr)
                    else:
                        self.loadvr(arg1,'t1')
                        self.loadvr(arg2,'t2')
                        instr = ArithmAssemblyLine('add', 't0', 't1', 't2')
                        self.FinalCodeFileUpdate(instr)
                        
                elif operator == '-':
                    if self.is_integer(arg1) and not(self.is_integer(arg2)):
                        instr = AssignAssemblyLine('t0', str(arg1))
                        self.FinalCodeFileUpdate(instr)
                        self.loadvr(arg2,'t1')
                        
                    
                    elif self.is_integer(arg2) and not(self.is_integer(arg1)):
                        self.loadvr(arg1,'t0')
                        instr = AssignAssemblyLine('t1', str(arg2))
                        self.FinalCodeFileUpdate(instr)
                        
                        
                    else:
                        self.loadvr(arg1,'t0')
                        self.loadvr(arg2,'t1')
                        
                    instr = ArithmAssemblyLine('sub', 't0', 't0', 't1')
                    self.FinalCodeFileUpdate(instr)
                
                elif operator in ['//', '%','*']:
                    if self.is_integer(arg1) and not(self.is_integer(arg2)):
                        instr = AssignAssemblyLine('t1', str(arg1))
                        self.FinalCodeFileUpdate(instr)
                        self.loadvr(arg2,'t2')
                    
                    elif self.is_integer(arg2) and not(self.is_integer(arg1)):
                        instr = AssignAssemblyLine('t2', str(arg2))
                        self.FinalCodeFileUpdate(instr)
                        self.loadvr(arg1,'t1')
                        
                    else:
                        self.loadvr(arg1,'t1')
                        self.loadvr(arg2,'t2')
                    
                    if operator == '//':
                        instr = ArithmAssemblyLine('div', 't0', 't1', 't2')
                    elif operator == '%':
                        instr = ArithmAssemblyLine('rem', 't0', 't1', 't2')
                    else:
                        instr = ArithmAssemblyLine('mul', 't0', 't1', 't2')
                    self.FinalCodeFileUpdate(instr)
                self.storevr(target, 't0')
                
            elif operator == 'jump':
                instr = JumpAssemblyLine('j', 'L_'+str(target))
                self.FinalCodeFileUpdate(instr)
            
            elif operator in ['==', '!=', '<', '>', '<=', '>=']:
                
                if self.is_integer(arg1) and not(self.is_integer(arg2)):
                    instr = AssignAssemblyLine('t1', str(arg1))
                    self.FinalCodeFileUpdate(instr)
                    self.loadvr(arg2,'t2')
                    
                elif self.is_integer(arg2) and not(self.is_integer(arg1)):
                    instr = AssignAssemblyLine('t2', str(arg2))
                    self.FinalCodeFileUpdate(instr)
                    self.loadvr(arg1,'t1')
                
                else:
                    self.loadvr(arg1,'t1')
                    self.loadvr(arg2,'t2')
                    
                if (operator == '=='):
                    instr =  BranchAssemblyLine('beq', 't1', 't2', 'L_'+str(target))
                elif (operator == '<'):
                    instr =  BranchAssemblyLine('blt', 't1', 't2', 'L_'+str(target))
                elif (operator == '>'):
                    instr =  BranchAssemblyLine('bgt', 't1', 't2', 'L_'+str(target))
                elif (operator == '<='):
                    instr  =  BranchAssemblyLine('ble', 't1', 't2', 'L_'+str(target))
                elif (operator == '>='):
                    instr  =  BranchAssemblyLine('bge', 't1', 't2', 'L_'+str(target))
                elif (operator == '!='):
                    instr  =  BranchAssemblyLine('bne', 't1', 't2', 'L_'+str(target))
                #print(instr)
                self.FinalCodeFileUpdate(instr)
                
            elif operator == 'ret':
                if self.is_integer(target):
                    instr = AssignAssemblyLine('t1', str(target))
                    self.FinalCodeFileUpdate(instr)
                else:
                    self.loadvr(target, 't1')
                    
                instr = MemAccessAssemblyLine('lw', 't0', -8, 'sp')
                self.FinalCodeFileUpdate(instr)
                instr = MemAccessAssemblyLine('sw', 't1', 0, 't0')
                self.FinalCodeFileUpdate(instr)
            
            elif operator == '=':
                
                if self.is_integer(arg1):
                    instr = AssignAssemblyLine('t0', str(arg1))
                    self.FinalCodeFileUpdate(instr)
                else:
                    self.loadvr(arg1, 't0')
                self.storevr(target, 't0')
            
            elif operator == 'in':
                instr = AssignAssemblyLine('a7', 5)
                self.FinalCodeFileUpdate(instr)
                self.FinalCodeFileUpdate('\tecall')
                self.storevr(arg1, 'a0')                
            
            elif operator == 'out':
                self.loadvr(arg1, 'a0')
                instr = AssignAssemblyLine('a7', 1)
                self.FinalCodeFileUpdate(instr)
                self.FinalCodeFileUpdate('\tecall')
                
                self.FinalCodeFileUpdate('\tla a0, str_nl')          
                instr = AssignAssemblyLine('a7', 4)
                self.FinalCodeFileUpdate('\tecall')
                
            elif operator == 'par':
                func_name =''
                tempQuad = currentQuad
                tempL = currentLabel
                
                
                while tempQuad.op != 'call':
                    tempL+=1
                    tempQuad = quadlist.getQuad(tempL)
                
                func_name = tempQuad.arg1
                #print(func_name)
                functionRecord = self.symTable.getRecord(func_name)
                
                
                if arg2 == 'cv':
                    tempOffset = 0
                    instr = ArithmAssemblyLine('addi', 'fp', 'sp', functionRecord[0].framelength)
                    self.FinalCodeFileUpdate(instr)
                    while currentQuad.op == 'par' and currentQuad.arg2 == 'cv':  
                        self.loadvr(currentQuad.arg1, 't0')
                        instr = MemAccessAssemblyLine('sw', 't0', '-'+str(12+4*tempOffset), 'fp')
                        self.FinalCodeFileUpdate(instr)
                        
                        tempOffset +=1
                        currentLabel+=1
                        currentQuad = quadlist.getQuad(currentLabel)
                        
                    else:
                        continue
                    
                elif arg2 == 'ret':
                    rec = self.symTable.getRecord(arg1)
                                       
                    instr = ArithmAssemblyLine('addi', 't0', 'sp', '-'+str(rec[0].offset))
                    self.FinalCodeFileUpdate(instr)
                    
                    instr = MemAccessAssemblyLine('sw', 't0', '-8', 'fp')
                    self.FinalCodeFileUpdate(instr)
                
                # elif for pass by reference etc
                
            elif operator == 'call':
                
                caller = self.symTable.getRecord(currentFuncName)
                callee = self.symTable.getRecord(currentQuad.arg1)
                # print('caller level: '+str(caller[1]))
                # print('callee level: '+str(callee[1]))
                
                
                # check if the caller and callee levels are the same level (i.e. if they have been called by the same function-parent)
                if caller[1] - callee[1] == 1:
                    instr = MemAccessAssemblyLine('lw', 't0', '-4', 'sp')
                    self.FinalCodeFileUpdate(instr)
                    
                    instr = MemAccessAssemblyLine('sw', 't0', '-4', 'fp')
                    self.FinalCodeFileUpdate(instr)
                else:    
                    instr = MemAccessAssemblyLine('sw', 'sp', '-4', 'fp')
                    self.FinalCodeFileUpdate(instr)
                
                instr = ArithmAssemblyLine('addi', 'sp', 'sp', callee[0].framelength)
                self.FinalCodeFileUpdate(instr)
                
                instr = JumpAssemblyLine('jal', 'L_'+callee[0].name)
                self.FinalCodeFileUpdate(instr)
                
                instr = ArithmAssemblyLine('addi', 'sp', 'sp', '-'+str(callee[0].framelength))
                self.FinalCodeFileUpdate(instr)               
            
            #print(str(currentQuad))
            currentLabel+=1
            currentQuad = quadlist.getQuad(currentLabel)
        else:
            self.createLabel(currentLabel)
        
    # Used to check if a string variable contains an integer
    def is_integer(self,s):
        return s.isdigit() or (s[0] == '-' and s[1:].isdigit())            
    
    def FinalCodeFileUpdate(self, word):
        try:           
            finalcodefile = open(self.timestamp+'_final.asm', 'a')
            #finalcodefile.write(str(self.currentLine)+':'+str(word)+'\n')
            finalcodefile.write(str(word)+'\n')
            self.currentLine+=1
            finalcodefile.close()
            
        except Exception as e:
            print('Error while creating or updating final code file:', e)            
            exit()

######################################### Main #########################################
def main():
    global currentLineNumber    
    
    currentLineNumber = 0
    
    testLexer = lexer(currentLineNumber, sys.argv[1]) 
    
    testParser = Parser(testLexer)
    testParser.syntax_analyzer()
    
    
main()