package com.uci.android101.sup;

import java.util.Date;
import java.util.UUID;

/**
 * Created by somarkoe on 11/5/16.
 */

public class Sup {

    private UUID mId;
    private Friend mFriend;
    private Date mDate;

    public Sup(Friend friend, Date date) {
        this.mId = UUID.randomUUID();
        this.mFriend = friend;
        this.mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public Friend getFriend() {
        return mFriend;
    }

    public void setFriend(Friend friend) {
        this.mFriend = friend;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

}
