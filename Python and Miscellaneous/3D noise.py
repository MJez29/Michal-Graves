#3D noise.py
from simplexnoise import *
from pygame import *

screen = display.set_mode((400, 400))

z = 0
freq = 1 / 25
sx = sy = s = 0

for x in range(50):
    for y in range(50):
        #Black = 0
        col = int(scaled_octave_noise_3d(3, 1/20, freq, 0, 5, x, y, z))
        if col == 0:
            rx = x
            ry = y

toCheck = [(rx, ry)]
hasChecked = []

while len(toCheck) > 0:
    nx, ny = toCheck.pop()
    if (nx, ny) not in hasChecked and int(scaled_octave_noise_3d(3, 1/20, freq, 0, 5, nx, ny, z)) == 0:
        sx += nx
        sy += ny
        s += 1
        toCheck.append((nx+1,ny))
        toCheck.append((nx-1,ny))
        toCheck.append((nx,ny+1))
        toCheck.append((nx,ny-1))
        hasChecked.append((nx, ny))

print(sx / s, sy  / s)
        

while True:
    for e in event.get():
        if e.type == QUIT:
            break;

    for x in range(80):
        for y in range(80):
            #Black = 0
            col = int(scaled_octave_noise_3d(3, 1/20, freq, 0, 5, x, y, z)) * 51
            
            draw.rect(screen, (col, col, col), (x * 5, y * 5, 5, 5))

    freq /= 1.01
    z += 1

    display.flip()
quit()
