package thing.imagemanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import java.util.ArrayList;
import java.util.List;

public class MultiInputProcessor implements InputProcessor {

    private List<InputProcessor> processors = new ArrayList();

    public MultiInputProcessor() {
        Gdx.input.setInputProcessor(this);
    }
    
    public void clear() {
        processors.clear();
    }
    
    public void addAtBack(InputProcessor processor) {
        if (processor instanceof MultiInputProcessor) {
            return;
        }
        
        if (!processors.contains(processor)) {
            processors.add(processor);
        } else {
            processors.remove(processor);
            processors.add(processor);
        }
    }
    
    public void addAtFront(InputProcessor processor) {
        if (processor instanceof MultiInputProcessor) {
            return;
        }
        
        if (!processors.contains(processor)) {
            processors.add(0, processor);
        } else {
            processors.remove(processor);
            processors.add(0, processor);
        }
    }
    
    public boolean remove(InputProcessor processor) {
        return processors.remove(processor);
    }
    
    public int size() {
        return processors.size();
    }
    
    @Override
    public boolean keyDown(int i) {
//        if (i == Input.Keys.F4 && (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT))) {
//            game.exit();
//            
//            return true;
//        }
        
        for (InputProcessor ps : processors) {
            if (ps.keyDown(i)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        for (InputProcessor ps : processors) {
            if (ps.keyUp(i)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        for (InputProcessor ps : processors) {
            if (ps.keyTyped(c)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        for (InputProcessor ps : processors) {
            if (ps.touchDown(i, i1, i2, i3)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        for (InputProcessor ps : processors) {
            if (ps.touchUp(i, i1, i2, i3)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        for (InputProcessor ps : processors) {
            if (ps.touchDragged(i, i1, i2)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        for (InputProcessor ps : processors) {
            if (ps.mouseMoved(i, i1)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        for (InputProcessor ps : processors) {
            if (ps.scrolled(i)) {
                return true;
            }
        }
        
        return false;
    }
}
