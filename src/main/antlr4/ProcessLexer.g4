lexer grammar ProcessLexer;

// Keys
POOL        : 'pool';
PROCESS     : 'process';
START       : 'start';
TASK        : 'task';
END         : 'end';
GATEWAY     : 'gateway';
SCOPE       : 'scope';
YES         : 'yes';
NO          : 'no';
MESSAGE : 'message';

fragment LOWER : [a-z];
fragment UPPER : [A-Z];

TaskType: 'MANUAL' | 'AUTOMATED' | 'USER';
GatewayType: 'EXCLUSIVE' | 'PARALLEL';

ID      : [a-zA-Z_][a-zA-Z_0-9]*;
STRING  : '"' (~["\\] | '\\' .)* '"' ;

LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '{';
RBRACE  : '}';
COMMA   : ',';
SEMICOLON: ';';
ARROW   : '->';

WS      : [ \t\r\n]+ -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;