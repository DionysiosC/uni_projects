# lab03.asm - Recursive palindrome string tester
#   coded in  MIPS assembly using MARS
# for MY?-505 - Computer Architecture, Fall 2021
# Department of Computer Science and Engineering, University of Ioannina

.globl pdrome

###############################################################################
.data
anna:  .asciiz "anna"
bobob: .asciiz "bobob"

###############################################################################
.text
  la    $a0, anna
  addi  $a1, $zero, 4
  jal   pdrome
  add   $s0, $v0, $zero  # keep the return value

  la    $a0, bobob
  addi  $a1, $zero, 5
  jal   pdrome
  add   $s1, $v0, $zero  # keep the return value
  # both s1 and s0 must be 1 here

  addiu   $v0, $zero, 10    # system service 10 is exit
  syscall                   # we are outa here.


pdrome:
###############################################################################	
	add	$v0, $0, $0 #make sure v0 is 0	
	sltiu	$t0, $a1, 2 # t0 = 0 if a1>2 else t0 = 1 i.e. if the size is 0 or 1, the string is palindrome
	beq	$t0, $0, basicpart
	addiu	$v0, $0, 1
	jr	$ra
	
basicpart:
	addi 	$sp, $sp, -12  # make room for 3 in stack
	
	sw   	$a0, 0($sp)# keep the substring pointer
	sw   	$a1, 4($sp)# keep the updated size
	sw   	$ra, 8($sp)# keep the return address
	

	
	lb	$t0, 0($a0)# first char of substring
	
	addi	$a1, $a1, -1
	add	$a0, $a1, $a0
	lb	$t1, 0($a0)# last char of substring
	
	bne	$t0, $t1, exit

	addi	$a1, $a1, -1
	sub	$a0, $a0, $a1 #next character
	
	
	jal pdrome
exit:
	lw   	$ra, 8($sp)
	addi 	$sp, $sp, 12 # empty the stack

###############################################################################
  	jr $ra
