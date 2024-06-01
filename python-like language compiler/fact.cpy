def factorial(n):
#{
    if n == 0:
        return 1
    else:
        return n*factorial(n - 1)
#}
#def main
#int n, result
n = 5
result = factorial(n)
print(result)  
