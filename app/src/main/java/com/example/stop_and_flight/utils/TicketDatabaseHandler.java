<<<<<<< HEAD:app/src/main/java/com/example/stop_and_flight/lee/TicketDatabaseHandler.java
package com.example.stop_and_flight.lee;
=======
package com.example.stop_and_flight.utils;
>>>>>>> origin/develop:app/src/main/java/com/example/stop_and_flight/utils/TicketDatabaseHandler.java

import com.example.stop_and_flight.lee.model.Ticket;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class TicketDatabaseHandler {
    private DatabaseReference mDatabase;

    public TicketDatabaseHandler(DatabaseReference mDatabase)
    {
        this.mDatabase = mDatabase;
    }

    public void insert_ticketDB(String uid, String date, Ticket ticket)
    {
        mDatabase.child("TICKET").child(uid).child(date).child(Integer.toString(ticket.getId())).setValue(ticket);
    }

    public void update_ticketDB(String uid, String date, String depart_time, String arrive_time, String Todo, int id,int requestcode)
    {
        String key = mDatabase.child("TICKET").child(uid).child(date).child(Integer.toString(id)).getKey();
        Ticket ticket = new Ticket(depart_time, arrive_time, Todo, id, "true",requestcode);

        Map<String, Object> Values = ticket.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/TICKET/" + uid + "/" + date + "/"+ key, Values);
        mDatabase.updateChildren(childUpdates);
    }

    public void delete_ticketDB(String uid, String date, Ticket ticket)
    {
        mDatabase.child("TICKET").child(uid).child(date).child(Integer.toString(ticket.getId())).removeValue();
    }
}
