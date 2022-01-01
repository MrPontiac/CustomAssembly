# Example File to display the first 20 fibonacci numbers
# Comments don't have to start with a pound as anything that is an invalid opcode doesn't do anything
# But the pound symbol does it explicitly searched for
# The runner is configured to use the 'asm' file in the running directory but that can be easily changed

#initalize the loop counter and start counting
mov loopCount 20
mov x 0
mov y 1
add
pta

loop:
mov y x
mov x a
add
pta

#we do two iterations before checking if we are done so the number to compare to here is 2
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