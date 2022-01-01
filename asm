mov loopCount 1476
mov x 0
mov y 1
add
pta
loop:
mov y x
mov x a
add
pta
mov tempA a
mov tempX x
mov y loopCount
mov x 2
jeq end
jgt end
mov x -1
add
mov loopCount a
mov a tempA
mov x tempX
jmp loop
end: