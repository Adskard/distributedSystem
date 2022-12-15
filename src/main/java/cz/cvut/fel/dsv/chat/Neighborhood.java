package cz.cvut.fel.dsv.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.Serializable;

@Log
@Getter
@Setter
public class Neighborhood implements Serializable {

    private Address next1;

    private Address next2;

    private Address prev;

    private Address leader;

    public Neighborhood(Address thisNode) {
        this.next1 = thisNode;
        this.next2 = thisNode;
        this.prev = thisNode;
        this.leader = thisNode;
    }

    private void fixTopology(){

    }
}
