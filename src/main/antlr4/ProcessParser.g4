parser grammar ProcessParser;

options {
    tokenVocab=ProcessLexer;
}

// Rules

model: pool+;

pool: 'pool' LPAREN ID COMMA STRING RPAREN LBRACE process RBRACE;

process: 'process' LPAREN ID COMMA STRING RPAREN 
         LBRACE startRule? flowElement* endRule* RBRACE;

flowElement: taskRef | gatewayRule | endRule;

taskRule: 'task' LPAREN ID COMMA STRING COMMA TaskType RPAREN;

startRule: 'start' LPAREN ID RPAREN SEMICOLON;

endRule: 'end' LPAREN ID RPAREN SEMICOLON;

gatewayRule:
    'gateway' LPAREN ID COMMA STRING COMMA GatewayType RPAREN 
    LBRACE (
        parallelScope+ | exclusiveScope
    ) RBRACE SEMICOLON;

parallelScope: 'scope' ARROW LBRACE flowElement+ RBRACE;

exclusiveScope: yesBranch noBranch;

yesBranch: 'yes' ARROW ( flowElement | LBRACE flowElement+ RBRACE );

noBranch: 'no' ARROW ( flowElement | LBRACE flowElement+ RBRACE );

taskRef
    : taskRule (ARROW messageRef)?  SEMICOLON
    | gatewayRule
    | ID (ARROW messageRef)? SEMICOLON;
       
messageRef: 'message' LPAREN ID RPAREN;
