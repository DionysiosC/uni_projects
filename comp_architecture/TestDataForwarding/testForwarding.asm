# ----------------------------------------------------------------------------------------
# testForwarding.asm 
#  Verify correctness of forwarding logic of the 5-stage pipelined MIPS processor
#    used in MYY505
# At exit, v1 will be 0 when all tests pass. Any other number indicates a mistake in pipeline control
# ----------------------------------------------------------------------------------------

# Gonna test 15 cases
.data
storage:
    .word 1
    .word 2
    .word 3

.text
# ----------------------------------------------------------------------------------------
# prepare register values.
# ----------------------------------------------------------------------------------------
#  DO NOT USE li as it breaks into 2 instructions and requires forwarding $at between them.
#  I use la here, but I should have assigned the address to $a0 differently
   la   $a0, storage
    addi $s0, $zero, 0
    addi $s1, $zero, 1
    addi $s2, $zero, 2
    addi $s3, $zero, 3
    
# ----------------------------------------------------------------------------------------
# Test 1: value forwarded from ALU to ALU without independed instruction
# -to input A of ALU
	addi $v1,   $zero, 1 
	add $t0, $s0, $s1 # produce new $t0 value
	add $t1, $t0, $s0 # forward new $t0 value to this instruction

	add  $t2,   $s1,   $s2  # dummy instruction
	add  $t2,   $s1,   $s2  # dummy instruction
	beq $s1, $t1, t1B # check for the desired results. Here, we check if t0 has been forwarded as expected
	j exit # if not, we exit with error status v1 = 1 or the number of the case
#the form of the other test cases is similar to this one.
# ---------------------
# -to input B of ALU
t1B:
	addi $v1,   $zero, 2 
	add $t0, $s0, $s1 # produce new $t0 value
	add $t1, $s0, $t0 # forward new $t0 value to this instruction

	add  $t2,   $s1,   $s2  # dummy instruction
	add  $t2,   $s1,   $s2  # dummy instruction
	beq $s1, $t1, t2A
	j exit
# ----------------------------------------------------------------------------------------
# Test 2 : value forwarded from ALU to ALU with independed instruction
# -to input A of ALU
t2A:
	addi $v1,   $zero, 3 
	add $t0, $s0, $s1 # produce new $t0 value
	add $zero, $t1,$t2
	add $t1, $t0, $s0 # forward new $t0 value to this instruction

	add  $t2,   $s1,   $s2  # dummy instruction
	add  $t2,   $s1,   $s2  # dummy instruction
	beq $s1, $t1, t2B
	j exit
# -to input B of ALU	
t2B:
	addi $v1,   $zero, 4 
	add $t0, $s0, $s1 # produce new $t0 value
	add $zero, $t1,$t2
	add $t1, $s0, $t0 # forward new $t0 value to this instruction

	add  $t2,   $s1,   $s2  # dummy instruction
	add  $t2,   $s1,   $s2  # dummy instruction
	beq $s1, $t1, t3A
	j exit

# Test 3: test forwarding of $t0 from ALU without independed instruction to branch.
# -to input A of beq comparator
t3A:
    addi $v1,$zero,5
	add $t0, $s1, $s2 # produce new $t0 value

    add  $t3,   $s1,   $s2  # dummy instruction
    beq  $t0,   $s3, t3b # t0 gets forwarded here
    j exit


# -to input B of beq comparator
t3b:
    addi $v1,$zero,6
	add $t0, $s1, $s2 # produce new $t0 value
	
    add  $t3,   $s1,   $s2  # dummy instruction
    beq  $s3,   $t0, t4a # t0 gets forwarded here
    j exit

# Test 4: test forwarding of $t0 from ALU with independed instruction to branch.
# -to input A of beq comparator
t4a:
    addi $v1,$zero,7
	add $t0, $s1, $s2 # produce new $t0 value
	add $zero, $t1,$t2
	
    add  $t3,   $s1,   $s2  # dummy instruction
    beq  $t0,   $s3, t4b # t0 gets forwarded here
    j exit
# -to input B of beq comparator
t4b:
    addi $v1,$zero,8
	add $t0, $s1, $s2 # produce new $t0 value
	add $zero, $t1,$t2

    add  $t3,   $s1,   $s2  # dummy instruction
    beq  $s3,   $t0, t5 # t0 gets forwarded here
    j exit 

# Test 5: test forwarding of $t0 from ALU without independed instruction to DMEM (sw)
t5:
	addi $v1,$zero,9
	add $t0, $s1, $zero # produce new $t0 value
	
	sw   $t0, 12($a0) # t0 gets forwarded here
	lw   $t4, 12($a0)

     	add  $t1,   $s1,   $s2  # dummy instruction
    	add  $t1,   $s1,   $s2  # dummy instruction
    	beq  $t4,   $s1,   t6 
    	j    exit 
 
# Test 6: test forwarding from ALU with independed instruction to DMEM (sw)  	
t6:
	addi $v1,$zero,10
	
	add $t0, $s1, $zero # produce new $t0 value
	add $zero, $t1,$t2	

	sw   $t0, 12($a0) # t0 gets forwarded here
	lw   $t4, 12($a0) # load to check

     	add  $t1,   $s1,   $s2  # dummy instruction
    	add  $t1,   $s1,   $s2  # dummy instruction
    	beq  $t4,   $s1,   t7a
    	j    exit 
 
# Test 7: test forwarding from DMEM (lw) to ALU 
# -to input A of ALU
t7a:
	addi $v1,$zero,11

	lw   $t0, 4($a0) # load 2 to t0
	add  $t2, $t0, $s1 # t0 gets forwarded here
	
    add  $t1,   $s1,   $s2  # dummy instruction
    add  $t1,   $s1,   $s2  # dummy instruction
    beq  $t2,   $s3,   t7b 
    j    exit

# -to input B of ALU
t7b:	
	addi $v1,$zero,12

	lw   $t0, 4($a0) # should load 2
	add  $t2, $s1, $t0 # t0 gets forwarded here
	
    add  $t1,   $s1,   $s2  # dummy instruction
    add  $t1,   $s1,   $s2  # dummy instruction
    beq  $t2,   $s3,   t8a 
    j    exit

# Test 8: test forwarding from DMEM (lw) to branch
# -to input A of beq comparator
t8a:
	addi $v1,$zero,13
	
	lw   $t0, 0($a0) # should load 1
	
    add  $t1,   $s1,   $s2  # dummy instruction
    add  $t1,   $s1,   $s2  # dummy instruction
    beq  $t0, $s1,   t8b
    j    exit		
    
# -to input B of beq comparator  
t8b:
	addi $v1,$zero,14
	
	lw   $t0, 0($a0) # load 1 to t0
	
    add  $t1,   $s1,   $s2  # dummy instruction
    add  $t1,   $s1,   $s2  # dummy instruction
    beq  $s1, $t0,   t9
    j    exit		
    
# Test 9: test forwarding from DMEM (lw) to DMEM (sw)	
t9:
	addi $v1,$zero,15
	
        lw   $t0, 8($a0)# load 3 to t0
        sw   $t0, 12($a0)  # should store 3
        lw   $t2, 12($a0)  #   load back the value from memory, to check it
        
        add  $t1,   $s1,   $s2  # dummy instruction
        add  $t1,   $s1,   $s2  # dummy instruction
        beq  $t2,   $s3,   pass
        j    exit
	
pass:
    add  $v1,   $zero, $zero  # previous write to zero is too far to affect this


exit:  # Check $v1. 0 means all tests pass, any other value is a unique error
    addiu      $v0, $zero, 10    # system service 10 is exit
    syscall
