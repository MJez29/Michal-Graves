#2D minecraft.py

from pygame import *
from random import *
from simplexnoise import *
import math
screen=display.set_mode((800,600))



spawnableBlocks=["dirt","grass","air"]
blockWidth=50
blockHeight=50
dirtBlockImg=image.load("dirt block.png").convert()
grassBlockImg=image.load("grass block.png").convert()
woodBlockImg=image.load("wood.png").convert()
leafBlockImg=image.load("leaf.png").convert()

biomeHeight=50
biomeWidth=200

blockHealthDict={"grass":5,"dirt":5,"wood":10,"leaf":2}

"""
for x in range(screen.get_width()//8):
    for y in range(screen.get_height()//6):
        gameMap.append([x,y,choice(spawnableBlocks)])
"""

def drawDirtBlock(surface,x,y,dirtBlockImg):
    surface.blit(dirtBlockImg,(x,y))

def Noise_1D(x):
    x = (x<<13) ^ x;
    #print(x * (x * x * 15731 + 789221) + 1376312589)
    return ( 1.0 - ( (x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);

def Smoothed_Noise_1D(x):
    return Noise_1D(int(x)) / 2  +  Noise_1D(int(x)-1)/4  +  Noise_1D(int(x)+1)/4

def Cosine_Interpolate(a,b,x):
    ft = x * 3.1415927
    f = (1 - math.cos(ft)) * 0.5

    return  a*(1-f) + b*f

def Interpolated_Noise_1D(x):
    int_x = int(x)
    fractional_x = x - int_x

    v1 = Smoothed_Noise_1D(int_x)
    v2 = Smoothed_Noise_1D(int_x + 1)

    return Cosine_Interpolate(v1, v2, fractional_x)

def Perlin_Noise_1D(x, persistance, octaves):
    total = 0
    p = persistance
    n = octaves

    for i in range(n):
        freq = 2**i
        amp = p**i

        total += Interpolated_Noise_1D(x * freq) * amp

    return total

def makeGameMap(width,height,blockWidth,blockHeight,dirtBlockImg,grassBlockImg,woodBlockImg,leafBlockImg):
    """Creates the game map and the map list
    gameMap - the physical map that is blitted onscreen
    mapList - the coordinates of each block arranged in a 2D list"""
    biome=Surface((width*blockWidth,height*blockHeight))
    mapList=[[] for i in range(width)]
    #print(mapList)
    """
    for x in range(width):
        
        #try :
        if x>0:
        #print(mapList[x-1])
            pos=mapList[x-1].index("grass")
            grassPos=choice([pos for i in range(10)]+[pos+1 for i in range(5)]+
                            [pos-1 for i in range(5)]+[pos+randint(-4,4) for i in range(2)])
        else :
            grassPos=randint(height//3,height//2)
            #mapList
            #print(mapList)
        makeTree=False
        heightOfTree=0
        if randint(0,25)==11:
            makeTree=True
            heightOfTree=randint(8,12)
        for y in range(height):
            if y<grassPos:
                if makeTree==True and grassPos-y<=heightOfTree:
                    biome.blit(woodBlockImg,(x*blockWidth,y*blockHeight))
                    mapList[x].append("wood")
                    if grassPos-y==heightOfTree:
                        for a in range(heightOfTree//2):
                            numOfLeaves=-(a//2)*(a-heightOfTree//2)+1
                            for b in range(numOfLeaves):
                                try:
                                    biome.blit(leafBlockImg,((x-b-1)*blockWidth,(y+a)*blockWidth))
                                    mapList[x-b-1][y+a]="leaf"
                                except IndexError: pass #error raised if spawned too close to the edge of the screen
                    
                else :
                    draw.rect(biome,(0,0,255),(x*blockWidth,y*blockHeight,blockWidth,blockHeight))
                    mapList[x].append("air")
            elif y==grassPos:
                biome.blit(grassBlockImg,(x*blockWidth,y*blockHeight))
                mapList[x].append("grass")
            elif y>grassPos:
                biome.blit(dirtBlockImg,(x*blockWidth,y*blockHeight))
                mapList[x].append("dirt")

    """
    #NEW STUFF
    xfreq = 1 / 240
    for x in range(width):
        grassPos = height - int(abs(Perlin_Noise_1D(x * xfreq, 1, 5)) * height + 15)
        #print(grassPos)
        for y in range(height):
            if y < grassPos:
                draw.rect(biome,(0,0,255),(x*blockWidth,y*blockHeight,blockWidth,blockHeight))
                mapList[x].append("air")
            elif y==grassPos:
                col = int(scaled_octave_noise_2d(5,1/10,1/5,0,4,x /5,y /4) + 0.5)
                #print(col, "   ", y)
                if col <= 1:
                    draw.rect(biome,(0,0,255),(x*blockWidth,y*blockHeight,blockWidth,blockHeight))
                    mapList[x].append("air")
                else :
                    biome.blit(grassBlockImg,(x*blockWidth,y*blockHeight))
                    mapList[x].append("grass")
            elif y>grassPos:
                col = int(scaled_octave_noise_2d(5,1/10,1/5,0,4,x /5,y /4) + 0.5)
                #print(col, "   ", y)
                if col <= 1:
                    draw.rect(biome,(0,0,255),(x*blockWidth,y*blockHeight,blockWidth,blockHeight))
                    mapList[x].append("air")
                else :
                    biome.blit(dirtBlockImg,(x*blockWidth,y*blockHeight))
                    mapList[x].append("dirt")

    for i in range(randint(10,20)):
        keepGoing = True
        xPos = randint(5,width - 5)
        while keepGoing:
            try:
                yPos = mapList[xPos].index("grass")
            except ValueError:
                yPos = mapList[xPos].index("dirt")
            
            while keepGoing:
                xPos += 1
                if mapList[xPos][yPos] == "air" and xPos < width - 2:
                    mapList[xPos][yPos] = "wood"
                    biome.blit(woodBlockImg,(xPos*blockWidth,yPos*blockHeight))
                else : keepGoing = False
                    
    image.save(biome, "biome.png")
    return biome,mapList


def movePlayer(keys,cur_x,cur_y):
    '''Moves the player if the WASD keys are pressed and if it's possible (checks collisions using collision map'''
    if keys[K_a]:
        cur_x-=4
    if keys[K_d]:
        cur_x+=4
    if keys[K_w]:
        cur_y-=2
    if keys[K_s]:
        cur_y+=2
    return cur_x,cur_y

class Player:
    "The class of the player"
    def __init__(self,mapList):

        #Player's coordinates are the top left pixel of them
        self.height=100 #height of the player
        self.width=50 #width of the player
        self.x=600
        #print(mapList[self.x//50])
        self.y=200#mapList[self.x//50].index("grass")*50-self.height
        self.moveSpeed=2 #how fast they move

        #Jumping
        self.onGround=True #flag of whether they're on the ground or not
        self.vx=0 #lateral velocity
        self.vy=0 #vertical velocity
        self.g=0.5 #force of gravity acting on the player
        

    def moveUp(self,mapList):
        "Moves the player upward when jumping"
        hitCeiling=False
        for i in range(int(self.vy*-1)):
            if hitCeiling==False:
                self.y -= 1
                if mapList[(self.x)//50][(self.y)//50]!="air" or mapList[(self.x+self.width-1)//50][(self.y)//50]!="air":
                    self.vy=0
                    hitCeiling=True
            
    def moveDown(self,mapList):
        "Moves the player down when falling"
        for i in range(int(self.vy)):
            if self.onGround==False:
                self.y+=1
                if mapList[(self.x)//50][(self.y+self.height)//50]!="air" or mapList[(self.x+self.width-1)//50][(self.y+self.height)//50]!="air":
                    self.y=self.y//50*50
                    self.onGround=True

    def move(self,keys,mapList):
        "Moves the player based on input from user"
        if self.onGround==False: #if they aren't on the ground
            self.vy+=self.g #updates velocity
            if self.vy<0: #if velocity is less than 0 they are jumping
                self.moveUp(mapList)
            elif self.vy>0: #otherwise they are falling
                self.moveDown(mapList)

        if keys[K_a]: #move to the left
            if self.onGround==True:
                if (mapList[(self.x-self.moveSpeed)//50][(self.y+self.height//2)//50]=="air" and
                    mapList[(self.x-self.moveSpeed)//50][(self.y)//50]=="air"): #if there arent any obstacles in the way
                    self.x-=self.moveSpeed
            else: #has to do 3 checks rather than 2 if they arent on the ground
                if (mapList[(self.x-self.moveSpeed)//50][(self.y+self.height//2)//50]=="air" and
                    mapList[(self.x-self.moveSpeed)//50][(self.y)//50]=="air" and
                    mapList[(self.x-self.moveSpeed)//50][(self.y+self.height)//50]=="air"):
                    self.x-=self.moveSpeed
        if keys[K_d]: #move to the right
            if self.onGround==True:
                if (mapList[(self.x+self.moveSpeed+self.width-1)//50][(self.y+self.height//2)//50]=="air" and
                    mapList[(self.x+self.moveSpeed+self.width-1)//50][(self.y)//50]=="air"):
                    self.x+=self.moveSpeed
            else:
                if (mapList[(self.x+self.moveSpeed+self.width-1)//50][(self.y+self.height//2)//50]=="air" and
                    mapList[(self.x+self.moveSpeed+self.width-1)//50][(self.y)//50]=="air" and
                    mapList[(self.x+self.moveSpeed+self.width-1)//50][(self.y+self.height)//50]=="air"):
                    self.x+=self.moveSpeed

        if (mapList[self.x//50][(self.y+self.height)//50]=="air" and
            mapList[(self.x+self.width)//50][(self.y+self.height)//50]=="air" and
            self.onGround==True): #if they walk off an edge
            self.onGround=False #no longer on ground
            self.vy=0 #start to fall
            

    def jump(self):
        self.vy=-10
        self.onGround=False
                
class MouseActions:
    "Class handling the breaking and creating of blocks"
    def __init__(self):
        self.block=None
        self.breakingBlock=False
        self.blockHealth=0

    def updateMousePos(self,mx,my):
        self.mx=mx
        self.my=my

    def highlightBlock(self,screen,blockWidth,blockHeight,cornerX,cornerY):
        surf=Surface((blockWidth,blockHeight))
        surf.fill((255,255,255))
        surf.set_alpha(125)
        screen.blit(surf,(self.mx-(self.mx+cornerX%blockWidth)%blockWidth,self.my-(self.my+cornerY%blockWidth)%blockHeight))

    def removeBlock(self,mapList,cornerX,cornerY,blockHealthDict):
        self.listX=(self.mx+cornerX)//50
        self.listY=(self.my+cornerY)//50
        if mapList[self.listX][self.listY]!="air":
            self.block=mapList[self.listX][self.listY]
            self.blockHealth=blockHealthDict[self.block]
            self.breakingBlock=True
            #print(self.block)
            #draw.rect(gameMap,(0,0,255),(listX*50,listY*50,blockWidth,blockHeight))

    def breakBlock(self,gameMap,mapList,blockWidth,blockHeight):
        self.blockHealth-=1
        #print(self.blockHealth)
        if self.blockHealth<0:
            mapList[self.listX][self.listY]="air"
            draw.rect(gameMap,(0,0,255),(self.listX*50,self.listY*50,blockWidth,blockHeight))
            self.breakingBlock=False
                
                
gameMap,mapList=makeGameMap(biomeWidth,biomeHeight,blockWidth,blockHeight,dirtBlockImg,grassBlockImg,woodBlockImg,
                            leafBlockImg)

time.set_timer(USEREVENT+1,100)
x,y=500,500
player=Player(mapList)
myClock=time.Clock()

mouseActions=MouseActions()

running=True
while running:
    k=key.get_pressed()
    mx,my=mouse.get_pos()
    mouseActions.updateMousePos(mx,my)
    for e in event.get():
        if e.type==QUIT:
            running=False
        if e.type==KEYDOWN and e.key==K_w and player.onGround:
            player.jump()
        if e.type==MOUSEBUTTONDOWN and e.button==1:
            mouseActions.removeBlock(mapList,x,y,blockHealthDict)
        if mouseActions.breakingBlock==True  and e.type==USEREVENT+1:
            mouseActions.breakBlock(gameMap,mapList,blockWidth,blockHeight)
        if e.type==USEREVENT+1:
            pass#print("Event!")

    player.move(k,mapList)
    #print(player.x,player.y)
    x=player.x+player.width//2-screen.get_width()//2
    y=player.y+player.height//2-screen.get_height()//2
    #print(x, " ", y)
    #print(x,y)
    #print(x,y)
    try :
        onscreen=gameMap.subsurface(Rect(x,y,screen.get_width(),screen.get_height())).copy()
        draw.rect(onscreen,(255,0,0),(player.x-x,player.y-y,player.width,player.height))
        mouseActions.highlightBlock(onscreen,blockWidth,blockHeight,x,y)
        screen.blit(onscreen,(0,0))
    except ValueError: pass
    
    #x+=1
    #y+=1

    #draw.circle(screen, (255,0,0), (mx,my), 3)
    
    myClock.tick(60)
    display.flip()
quit()
