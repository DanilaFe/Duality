package controls;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class ControlManager {

    private Array<ControlData> controls;
    private Array<Integer> groupKeys;

    public ControlManager() {
        loadControls();
        loadGroupKeys();
    }

    private void loadControls() {
        Array<Controller> controllers = Controllers.getControllers();
        controls = new Array<>();
        controls.add(new ControlData(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE, Input.Keys.UP, null));
        controls.add(new ControlData(Input.Keys.S, Input.Keys.D, Input.Keys.SHIFT_LEFT, Input.Keys.W, null));
    }

    private void loadGroupKeys() {
        groupKeys = new Array<>();
        groupKeys.add(Input.Keys.A);
        groupKeys.add(Input.Keys.B);
        groupKeys.add(Input.Keys.C);
    }

    public ControlData getNthControl(int id) {
        if (id < controls.size) return controls.get(id);
        else return controls.get(0);
    }

    public int getNthGroupKey(int id) {
        if (id < groupKeys.size) return groupKeys.get(id);
        else return groupKeys.get(0);
    }

}
