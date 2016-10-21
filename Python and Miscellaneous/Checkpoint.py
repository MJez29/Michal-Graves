#Checkpoint.py

from pygame import *
from math import *

imgs = []
for i in [1,2,3]:
    imgs.append(image.load("Checkpoint Red %d.png" %i))

screen=display.set_mode((250,400))

myClock = time.Clock()

n = 0
op = 1

running=True
while running:
    for e in event.get():
        if e.type==QUIT:
            running=False

    screen.blit(imgs[n // 10], (0,0))
    n += op
    if (n > 29):
        n = 29
        op *= -1
    elif (n < 0):
        n = 0
        op *= -1

    myClock.tick(45)
    display.flip()
    
quit()
