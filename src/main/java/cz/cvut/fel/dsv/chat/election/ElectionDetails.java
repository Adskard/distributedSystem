package cz.cvut.fel.dsv.chat.election;

import lombok.Getter;
import lombok.Setter;

/**
 * Data class for node election information aggregation.
 *
 */
@Getter
@Setter
public class ElectionDetails {

    private Long virtualId;
    private ElectionState state;

    private Long prevId;

    private Long prev2Id;
}
