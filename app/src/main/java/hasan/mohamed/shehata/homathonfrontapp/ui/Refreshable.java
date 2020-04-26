package hasan.mohamed.shehata.homathonfrontapp.ui;

import java.util.ArrayList;

public interface Refreshable {
    public static ArrayList<Refreshable> instances = new ArrayList<>();
    public Object refresh(Object...data);
}
