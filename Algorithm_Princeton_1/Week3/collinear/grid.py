from random import randint

def main():
    output = open('random.txt', 'w');
    result = [];
    target = 100;
    number = 0;
    output.write(str(target) + '\n')

    while(number < target):
        x = randint(0, 9)
        y = randint(0, 9)
        print x, y;
        if (x, y) not in result:
            number += 1
            result.append((x, y))
            output.write(str(x) + "\t" + str(y) + "\n");

main()