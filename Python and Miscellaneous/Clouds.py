#Clouds.py

from simplexnoise import *
from pygame import *

screen = display.set_mode((800, 600))

xFreq = 1 / 20
yFreq = 1 / 4.5

for x in range(160):
    for y in range(120):
        n = scaled_octave_noise_2d(5, 1/10, 1/5, -10, 18, x * xFreq, y * yFreq)
        if n < 10:
            col = (0, 0, 255)
        elif n < 11:
            col = (50, 50, 255)
        elif n < 12:
            col = (100, 100, 255)
        elif n < 13:
            col = (150, 150, 255)
        elif n < 14:
            col = (200, 200, 255)
        else:
            col = (255, 255, 255)
        draw.rect(screen, col, (x * 5, y * 5, 5, 5))

running=True
while running:
    for e in event.get():
        if e.type==QUIT:
            running=False



    display.flip()
quit()
