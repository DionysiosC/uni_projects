# lab02.asm - Pairwise swap in an array of 32bit integers
#   coded in  MIPS assembly using MARS
# for MYÎ¥-505 - Computer Architecture, Fall 2021
# Department of Computer Science and Engineering, University of Ioannina

        .globl swapArray # declare the label as global for munit
        
###############################################################################
        .data
array: .word 5, 6, 7, 8, 1, 2, 3, 4

###############################################################################
        .text 
# label main freq. breaks munit, so it is removed...
        la         $a0, array
        li         $a1, 8
	

swapArray:

	beq	$a1,$zero,exit
	
	sll	$t0, $a1, 1 # size/2
	add	$t1, $a0, $t0 #pointer	in the middle of the arra
	add	$a1,$t1,$zero
	
loop:
	lw	$t2, 0($a0)
	lw	$t3, 0($t1)
	sw	$t2, 0($t1)
	sw	$t3, 0($a0)
	
	addiu	$a0,$a0,4
	addiu	$t1,$t1,4
	
	
	bne	$a0, $a1, loop

exit:
        addiu      $v0, $zero, 10    # system service 10 is exit
        syscall                      # we are outta here.

		
		
