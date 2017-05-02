//package thing.imagemanager;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.graphics.GL30;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OldImageGridFrame extends Actor implements InputProcessor {
//
//    public static final int THUMBNAIL_WIDTH = 150;
//    public static final int SCROLL_BAR_WIDTH = 25;
//    public static final int MAX_ITEMS = 200;
//
//    private List<ImageInfo> images;
//    private ImageSelectListener listener;
//
//    private ImagePreviewFrame preview;
//
//    private float scroll = 0;
//    private float targetScroll = scroll;
//
//    private boolean draggingScroll = false;
//    private int draggingScrollYOffset = 0;
//
//    private boolean contextMenu = false;
//    private int contextX = 0;
//    private int contextY = 0;
//    private static final int CONTEXT_WIDTH = 50;
//    private static final int CONTEXT_HEIGHT = 80;
//    private static final String CONTEXT_OPTION1 = "Edit";
//    private static final String CONTEXT_OPTION2 = "Reload";
//    private static final String CONTEXT_OPTION3 = "Open";
//    private static final String CONTEXT_OPTION4 = "Folder";
//    private ImageInfo contextImage;
//
//    public OldImageGridFrame() {
//        preview = new ImagePreviewFrame();
//        preview.setSize(getWidth(), getHeight());
//        preview.setPosition(getX(), getY());
//        setImageSelectListener(preview);
//    }
//
//    public void setImageSelectListener(ImageSelectListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public void setSize(float width, float height) {
//        super.setSize(width, height);
//
//        preview.setSize(width, height);
//    }
//
//    @Override
//    public void setPosition(float x, float y) {
//        super.setPosition(x, y);
//
//        preview.setPosition(x, y);
//    }
//
//    public void setImageList(ArrayList<ImageInfo> imageList) {
//        images = imageList;
//        if (images.size() > MAX_ITEMS) {
//            images = images.subList(0, MAX_ITEMS);
//        }
//        scroll = 0;
//        targetScroll = 0;
//    }
//
//    @Override
//    public void act(float delta) {
//        updateScrollTravel();
//    }
//
//    private void updateScrollTravel() {
//        if (scroll != targetScroll) {
//            scroll += (targetScroll - scroll) / 4;
//
//            if (Math.abs(scroll - targetScroll) <= 0.01) {
//                scroll = targetScroll;
//            }
//        }
//    }
//
//    private float getScrollRenderY() {
//        return getY() + getHeight() - GUI.border - 2 - SCROLL_BAR_WIDTH * 2 - (getHeight() - SCROLL_BAR_WIDTH * 2 - GUI.border * 2 - 4) * scroll;
//    }
//
//    private float getScrollFromY(float y) {
//        float length = getHeight() - GUI.border * 2 - 4 - SCROLL_BAR_WIDTH * 2;
//        float relative = y - getY() - GUI.border - 2;
//        float result = 1 - relative / length;
//        if (result < 0) {
//            result = 0;
//        }
//        if (result > 1) {
//            result = 1;
//        }
//
//        return result;
//    }
//
//    private float getScrollRenderX() {
//        return getX() + getWidth() - GUI.border - SCROLL_BAR_WIDTH + 2;
//    }
//
//    @Override
//    public boolean keyDown(int i) {
//        return false;
//    }
//
//    @Override
//    public boolean keyUp(int i) {
//        return false;
//    }
//
//    @Override
//    public boolean keyTyped(char c) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDown(int x, int y, int finger, int button) {
//        y = Gdx.graphics.getHeight() - y;
//
//        if (x > getX() + GUI.border && x < getX() + getWidth() - GUI.border && y > getY() + GUI.border && y < getY() + getHeight() - GUI.border) {
//            //Within actor
//
//            if (button == 0 && !draggingScroll && x > getScrollRenderX() && x < getScrollRenderX() + SCROLL_BAR_WIDTH - 4 && y > getScrollRenderY() && y < getScrollRenderY() + SCROLL_BAR_WIDTH * 2) {
//                //Scrollbar dragging
//                draggingScroll = true;
//                draggingScrollYOffset = (int) getScrollRenderY() - y;
//                return true;
//            }
//
//            if (contextMenu) {
//                contextMenu = false;
//
//                if (x > contextX && x <= contextX + CONTEXT_WIDTH && y > contextY-CONTEXT_HEIGHT && y <= contextY) {
//                    //Clicked in context menu
//
//                    if (y > contextY - 20) {
//                        System.out.println("Context 1 clicked");
//                    } else if (y <= contextY - 20 && y > contextY - 40) {
//                        System.out.println("Context 2 clicked");
//                    } else if (y <= contextY - 40 && y > contextY - 60) {
//                        System.out.println("Context 3 clicked");
//                    } else if (y <= contextY - 60) {
//                        System.out.println("Context 4 clicked");
//                    }
//                }
//            } else if (button == 0 && listener != null) {
//                selectedImage(x, y);
//            } else if (button == 1) {
//                rightClickedImage(x, y);
//            }
//        }
//
//        return false;
//    }
//
//    private void rightClickedImage(int x, int y) {
//        for (int i = 0; i < images.size(); i++) {
//            if (x > getImageXFromIndex(i) && x < getImageXFromIndex(i) + THUMBNAIL_WIDTH && y > getImageYFromIndex(i) && y < getImageYFromIndex(i) + THUMBNAIL_WIDTH) {
//                contextMenu = true;
//                contextImage = images.get(i);
//                contextX = x;
//                contextY = y;
//
//                break;
//            }
//        }
//    }
//
//    private void selectedImage(int x, int y) {
//        //Click on image
//        for (int i = 0; i < images.size(); i++) {
//            if (x > getImageXFromIndex(i) && x < getImageXFromIndex(i) + THUMBNAIL_WIDTH && y > getImageYFromIndex(i) && y < getImageYFromIndex(i) + THUMBNAIL_WIDTH) {
//                listener.imageSelected(images.get(i));
//                break;
//            }
//        }
//    }
//
//    @Override
//    @SuppressWarnings("UnusedAssignment")
//    public boolean touchUp(int x, int y, int finger, int button) {
//        y = Gdx.graphics.getHeight() - y;
//
//        if (draggingScroll) {
//            draggingScroll = false;
//        }
//
//        return false;
//    }
//
//    @Override
//    public boolean touchDragged(int x, int y, int finger) {
//        y = Gdx.graphics.getHeight() - y;
//
//        if (draggingScroll) {
//            targetScroll = getScrollFromY(y + draggingScrollYOffset);
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public boolean mouseMoved(int i, int i1) {
//        return false;
//    }
//
//    @Override
//    public boolean scrolled(int i) {
//        if (images != null && Gdx.input.getX() >= getX() && Gdx.input.getX() < getX() + getWidth() && Gdx.graphics.getHeight() - Gdx.input.getY() >= getY() && Gdx.graphics.getHeight() - Gdx.input.getY() < getY() + getHeight()) {
//            int horizontalGridCount = getMaxHorizontalImages();
//            int verticalGridCount = getVerticalImageCount(horizontalGridCount);
//            targetScroll += i * (1.0 / verticalGridCount);
//
//            if (targetScroll < 0) {
//                targetScroll = 0;
//            }
//            if (targetScroll > 1) {
//                targetScroll = 1;
//            }
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        preview.act(Gdx.graphics.getDeltaTime());
//
//        drawFrame(batch);
//
//        //Set up frame scissor
//        Gdx.gl.glEnable(GL30.GL_SCISSOR_TEST);
//        Gdx.gl.glScissor((int) (getX() + GUI.border * 2), (int) (getY() + GUI.border * 2), (int) (getX() + getWidth() - GUI.border * 4), (int) (getY() + getHeight() - GUI.border * 4));
//        batch.begin();
//
//        drawImageIcons(batch);
//
//        drawContextMenu(batch);
//
//        //End frame scissor
//        GUI.ensureEnded(batch);
//        Gdx.gl.glDisable(GL30.GL_SCISSOR_TEST);
//        batch.begin();
//
//        preview.draw(batch, parentAlpha);
//    }
//
//    private void drawFrame(Batch batch) {
//        //Draw frame
//        GUI.ensureEnded(batch);
//        GUI.ensureBegan(GUI.sr, ShapeRenderer.ShapeType.Filled);
//        GUI.sr.setColor(GUI.frame);
//        GUI.sr.rect(getX() + GUI.border, getY() + GUI.border, getWidth() - GUI.border * 2, getHeight() - GUI.border * 2);
//        GUI.sr.setColor(GUI.trim);
//        GUI.sr.rectLine(getX() + getWidth() - GUI.border - SCROLL_BAR_WIDTH, getY() + GUI.border, getX() + getWidth() - GUI.border - SCROLL_BAR_WIDTH, getY() + getHeight() - GUI.border, 1);
//        GUI.sr.rect(getScrollRenderX(), getScrollRenderY(), SCROLL_BAR_WIDTH - 4, SCROLL_BAR_WIDTH * 2);
//        GUI.sr.end();
//    }
//
//    private void drawImageIcons(Batch batch) {
//        GUI.ensureEnded(GUI.sr);
//        GUI.ensureBegan(batch);
//        boolean imageLoadedThisFrame = false;
//
//        //Draw images
//        if (images != null && !images.isEmpty()) {
//            for (int i = 0; i < images.size(); i++) {
//                ImageInfo img = images.get(i);
//
//                float x = getImageXFromIndex(i);
//                float y = getImageYFromIndex(i);
//
//                //Draw
//                if (y < getY() + getHeight() && y > getY() - THUMBNAIL_WIDTH) {
//                    if (!img.isLoaded()) {
//                        if (!imageLoadedThisFrame) {
//                            Utils.renderTextureFitToArea(batch, img.getTexture(), x, y, THUMBNAIL_WIDTH, THUMBNAIL_WIDTH);
//                        }
//
//                        imageLoadedThisFrame = true;
//                    } else {
//                        Utils.renderTextureFitToArea(batch, img.getTexture(), x, y, THUMBNAIL_WIDTH, THUMBNAIL_WIDTH);
//                    }
//
//                    if (Gdx.input.getX() > x && Gdx.input.getX() < x + THUMBNAIL_WIDTH && Gdx.graphics.getHeight() - Gdx.input.getY() > y && Gdx.graphics.getHeight() - Gdx.input.getY() < y + THUMBNAIL_WIDTH) {
//                        batch.end();
//                        GUI.sr.begin(ShapeRenderer.ShapeType.Line);
//                        GUI.sr.setColor(GUI.trim);
//                        GUI.sr.rect(x, y, THUMBNAIL_WIDTH, THUMBNAIL_WIDTH);
//                        GUI.sr.end();
//                        batch.begin();
//                    }
//                }
//            }
//        }
//    }
//
//    private void drawContextMenu(Batch batch) {
//        //Draw context menu
//        if (contextMenu) {
//            GUI.ensureEnded(batch);
//            GUI.ensureBegan(GUI.sr, ShapeRenderer.ShapeType.Filled);
//
//            //Draw frame
//            GUI.sr.setColor(GUI.frame);
//            GUI.sr.rect(contextX, contextY - CONTEXT_HEIGHT, CONTEXT_WIDTH, CONTEXT_HEIGHT);
//
//            //Draw seperators
//            GUI.sr.setColor(GUI.trim);
//            GUI.sr.rect(contextX, contextY - 25 + 3, CONTEXT_WIDTH, 1);
//            GUI.sr.rect(contextX, contextY - 45 + 3, CONTEXT_WIDTH, 1);
//            GUI.sr.rect(contextX, contextY - 65 + 3, CONTEXT_WIDTH, 1);
//
//            GUI.ensureEnded(GUI.sr);
//            GUI.ensureBegan(batch);
//
//            Utils.renderTextToFit(batch, CONTEXT_OPTION1, contextX, contextY - 5, CONTEXT_WIDTH);
//            Utils.renderTextToFit(batch, CONTEXT_OPTION2, contextX, contextY - 25, CONTEXT_WIDTH);
//            Utils.renderTextToFit(batch, CONTEXT_OPTION3, contextX, contextY - 45, CONTEXT_WIDTH);
//            Utils.renderTextToFit(batch, CONTEXT_OPTION4, contextX, contextY - 65, CONTEXT_WIDTH);
//        }
//    }
//
//    private float getImageXFromIndex(int index) {
//        int xIndex = index % getMaxHorizontalImages();
//        float x = getX() + GUI.border * 2 + (xIndex * (GUI.border + THUMBNAIL_WIDTH));
//
//        return x;
//    }
//
//    private float getImageYFromIndex(int index) {
//        int yIndex = index / getMaxHorizontalImages();
//        int scrollibleLength = getVerticalGridHeight();
//        scrollibleLength -= (getHeight() - GUI.border * 4);
//        if (scrollibleLength < 0) {
//            scrollibleLength = 0;
//        }
//        float y = getY() + getHeight() - GUI.border * 2 - THUMBNAIL_WIDTH - (yIndex * (THUMBNAIL_WIDTH + GUI.border)) + (scroll * scrollibleLength);
//
//        return y;
//    }
//
//    private int getVerticalGridHeight() {
//        int horizontalGridCount = getMaxHorizontalImages();
//        int verticalGridCount = getVerticalImageCount(horizontalGridCount);
//        int verticalGridHeight = (THUMBNAIL_WIDTH + GUI.border) * verticalGridCount;
//        return verticalGridHeight;
//    }
//
//    private int getVerticalImageCount(int horizontalGridCount) {
//        return (int) Math.ceil((float) images.size() / horizontalGridCount);
//    }
//
//    private int getMaxHorizontalImages() {
//        return (int) Math.floor((getWidth() - 2 * GUI.border - SCROLL_BAR_WIDTH) / (THUMBNAIL_WIDTH + GUI.border));
//    }
//}
