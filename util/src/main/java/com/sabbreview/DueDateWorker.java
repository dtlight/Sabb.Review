import com.sabbreview.model.Assignment;

import java.time.LocalDate;

public static class DueDateWorker implements Runnable extends Controller{

    public void run{
            System.out.println(LocalDate.now());
            em.getTransaction().begin();
            Assignment assignment = em.find(Assignment.class, assignmentID);
    }
}