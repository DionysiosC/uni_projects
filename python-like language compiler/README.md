
# A Python-like language Compiler

## Table of Contents:
1. [Introduction](#introduction)
2. [The cpy Language](#the-cpy-language) 
3. [Implementation](#implementation)
4. [Basic Operation](#basic-operation) 
5. [Final Notes](#final-notes)

## Introduction
Here we have a compiler program written by me, using Python 3.12.2, in 2024, in the context of the compilers course of the dpt. of computer science and engineering in the University of Ioannina. The compiler does not use any optimization and is created to compile (most of the time!) any program written in a python-like language called 'cpy'. It is a language that is accompanied by a small grammar and a small set of key-words. A small analysis of this language is given in this file. Last but not least, the compiler creates three files after being run: one that contains a form of the intermediate code created and used by it, one that contains the symbol tables (the bottom one is the latest) and one that contains the final code (assembly) that targets the [RISC-V](https://riscv.org/) processor ISA.
## The cpy language
The cpy language is an educational language created by the professor of the course. The most basic characteristics of this language must be four: it does not support any 'for' loop, it supports only integer ('int') type values and variables, the user needs to declare each variable using the '#int' keyword and every variable is passed to each function by value only ('cv'). Other than some other small differences, the language looks a lot like Python in terms of syntax and key-words. In fact, any program written using cpy should be able to be used with a Python interpreter without any problems. One other major difference is the no need to use indentations (as we use in python).
Here is the grammar of the cpy language:                
>**program       :**     declarations functions main
                ;

>**declarations    :**     ( '#int' id_list )*
                ;

>**id_list         :**     ID ( ',' ID )*
                ;

>**glob_decl       :**     ( 'global' id_list )*
                ;

>**functions       :**   function*
                ;

>**function        :**   'def' ID '(' id_list ')' ':'
                    '#{'
                        declarations
                        functions
                        glob_decl
                        code_block
                    '#}'
                ;

>**main            :**   '#def' 'main'
                        declarations
                        code_block
                ;

>**code_block      :**   statement+
                ;

>**statement       :**   simple_statement
                |   structured_statement
                ;

>**simple_statement
                :**   assignment_stat
                |   print_stat
                |   return_stat
                ;


>**structured_statement
                :**   if_stat
                |   while_stat
                ;

>**assignment_stat :**   ID '=' ( expression
                           | 'int' '(' 'input' '(' ')'
                           )
                ;


>**print_stat      :** 'print' '(' expression ')'
                ;

>**return_stat     :** 'return' expression
                ;

>**statement_or_block
                :** statement
                | '#{' statement+ '#}'
                ;

>**if_stat         :**   'if' condition ':'
                        statement_or_block
                    ('elif' condition':'
                        statement_or_block )*
                    (   'else' ':'
                        statement_or_block )?
                ;

>**while_stat      :**   'while' condition ':'
                        statement_or_block
		        ;

>**expression      :**   optional_sign term ( add_oper term )*
                ;

>**term            : **  factor ( mul_oper factor )*
                ;
	    
>**factor          :**   INTEGER
		        |   '(' expression ')'
                |   ID idtail
                ;

>**idtail          :**   '(' actual_par_list ')'
                |
                ;


>**actual_par_list :**   expression ( ',' expression )*
                |
                ;

>**condition       :**  bool_term ( 'or' bool_term )*
                ;

>**bool_term       :**   bool_factor ( 'and' bool_factor )*
                ;

>**bool_factor     :**   expression rel_op expression
                |   'not' expression rel_op expression
                ;

>**optional_sign   :**   add_oper
                |
                ;

>**add_oper        :**   '+' | '-'
                ;

>**mul_oper        :**   '*' | '//' | '%'
                ;

>**rel_op          :**   '>' | '<' | '>=' | '<=' | '==' | '!='
                ;
                
>**INTEGER			:**	[0‐9]+
						;
	
>**ID				:** [a‐zA‐Z][a‐zA‐Z0‐9]* 
						;


Here we have the keywords used by the language stored in an array:
>['main', 'def', '#def', '#int', 'global','if', 'elif', 'else', 'while', 'print', 'return','input', 'int', 'and', 'or', 'not']

Note: the comments must be one liners only and must be included in '##' symbols (for example: ## comment ##)

## Implementation
Although the implementation language is python, a good effort was made to stay true to OOP ideals. In other words, classes and inheritance are elements found in the program, but not the same is true about encapsulation. As a matter of fact, there are 4 major classes found in the program: lexer, Parser, SymbolTable and FinalCode. There is also the Token class used by the lexer class and some classes (namely Quad and QuadList) classes used for the creation and management of the intermediate code. Of course, there are many more classes found in the program.

## Basic operation
The program works in this way: After a lexer object has been created that just opens the file, it creates an array and every code line of the cpy program to be compiled is stored in it. Then, a parser object is created that takes as an argument the lexer object. Lastly, the ***syntax_analyzer*** function is called by the parser object and that starts the 'chain of compilation' so to speak. To put it simply, when the syntax_analyzer function is called, the lexer feeds the parser with tokens by the get_token function, where each token contains information about every word of the code file. Then, wherever is appropriate, the symbol table and intermediate code files get updated and assembly code is been generated whenever the compiler reaches the end of any function declaration (the main included).
Of course, during the compilation process, the compiler checks the code for lexical errors (i.e. not recognized key-words etc), syntax errors (i.e. return statement missing etc) and semantic errors (i.e. the 'a' variable has not been declared etc).
## Final Notes
The program, like every other project I completed in the context of my uni courses, is not 100% functional and that is not the point at all. The point is that I learned a lot while creating this program and that I am happy with the final result, the one you find in this repository.
Other than that, some known issues happen with nested function declarations and the compiler does not handle global variables very well. In the repository, you can find two .'cpy' files that this compiler compiles successfully.
To use this compiler, you must open a cmd (or bash in Linux) and use this syntax:
`python cpy_compiler.py <your_program>.cpy`
Then, if no errors occur, you will find three new files in the current directory. Their titles are explanatory. Feel free to test the quality of this compiler by assembling the final code using a RISC-V runtime simulator (the obvious choice must be [RARS](https://github.com/TheThirdOne/rars))
