package gui;

/**
 * @author austinbt
 */
public interface SliderListener {
    void sliderDragged(Slider slider, int value);

    void sliderDropped(Slider slider, int value);
}
