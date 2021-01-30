package model;

import java.util.ArrayList;
import java.util.List;

public class Family {
    private int relateEmployeeNum;
    private List<FamilyMember> familyMemberList;

    public Family(int relateEmployeeNum) {
        this.relateEmployeeNum = relateEmployeeNum;
        List<FamilyMember> familyMemberList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            familyMemberList.add(new FamilyMember(STATUS_HEALTHY));
        }
        this.familyMemberList = familyMemberList;
    }

    static class FamilyMember {
        private int healthyStatus;

        public FamilyMember(int healthyStatus) {
            this.healthyStatus = healthyStatus;
        }

        public int getHealthyStatus() {
            return healthyStatus;
        }
    }

    // healthy status
    private static int STATUS_HEALTHY = 0;
    private static int STATUS_INFECTED = 1;
}
