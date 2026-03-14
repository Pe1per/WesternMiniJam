public class RenderObject {
    int x, y;
    Sprite[] sprite = new Sprite[6];
    String[] paths;

    public RenderObject(int x, int y, String[] paths) {
        this.x = x;
        this.y = y;
        this.paths = paths;

        for(int i = 0; i<paths.length; i++){
            this.sprite[i] = Sprite.loadSprite(paths[i]);
        }
    }
}
