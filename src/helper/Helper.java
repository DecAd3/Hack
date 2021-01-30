package helper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import model.RoomModel.Room;
import model.RoomModel.Room.STATUS;

public class Helper {

    public static Room getFreeRoom(List<Room> RoomList) {
        AtomicReference<Room> rooms = new AtomicReference<Room>();
        RoomList.forEach(m -> {
            if ((m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity()))) {
                rooms.set(m);
            }
        });
        return rooms.get();
    }
}
