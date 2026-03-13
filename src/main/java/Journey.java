public class Journey {
    long window;
    RenderObject horse;

    public Journey(long window){
        this.window = window;

        horse = new RenderObject(200,400, Sprite.loadSprite("/horse/sprint/sprint01.png"));
    }

    public void render(Render r){
        r.clearScreen(0xFFFFFFFF);
        r.drawSprite(horse.sprite.pixels,horse.sprite.width,horse.sprite.height,horse.x,horse.y);
    }
}
