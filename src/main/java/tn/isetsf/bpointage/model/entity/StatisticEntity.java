package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticEntity {
    private String label;
    private List<Integer> data=new ArrayList<>();
    public void addData(int data)
    {
        this.data.add(data);
    }
}
