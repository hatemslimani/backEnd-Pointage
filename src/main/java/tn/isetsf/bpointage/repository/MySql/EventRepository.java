package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.EventModel;

import java.sql.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventModel,Integer> {
    @Query("select e from EventModel e where e.start<= :toDay and e.end >= :toDay")
    List<EventModel> getEventsByDate(Date toDay);
}
