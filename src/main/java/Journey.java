public class Journey {
    long window;
    int framecounter;
    int frame;

    String[] sprint_paths = {"/horse/sprint/sprint01.png","/horse/sprint/sprint02.png","/horse/sprint/sprint03.png","/horse/sprint/sprint04.png","/horse/sprint/sprint05.png","/horse/sprint/sprint06.png"};
    String[] saloon_path = {"/buildings/saloon.png"};
    RenderObject horse;
    RenderObject saloon;

    public Journey(long window,int framecounter){
        this.window = window;
        this.framecounter = framecounter;

        //initialize permanent objects
        horse = new RenderObject(200,400, sprint_paths);
        saloon = new RenderObject(100, 150, saloon_path);
    }

    public void render(Render r, int framecounter){
        frame = (framecounter / 5) % 6;
        r.clearScreen(0xFFFFFFFF);

        r.drawSprite(saloon.sprite[0].pixels,saloon.sprite[0].width,saloon.sprite[0].height,saloon.x,saloon.y);
        r.drawSprite(horse.sprite[frame].pixels,horse.sprite[frame].width,horse.sprite[frame].height,horse.x,horse.y);
    }
}
