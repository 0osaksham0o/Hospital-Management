package com.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "RoomNumber", nullable = false)
    private Integer roomNumber;

    @Column(name = "RoomType", nullable = false, length = 30)
    private String roomType;

    @Column(name = "BlockFloor", nullable = false)
    private Integer blockFloor;

    @Column(name = "BlockCode", nullable = false)
    private Integer blockCode;

    @Column(name = "Unavailable", nullable = false)
    private Boolean unavailable;

    public Room() {}

    public Room(Integer roomNumber, String roomType,
                Integer blockFloor, Integer blockCode, Boolean unavailable) {
        this.roomNumber  = roomNumber;
        this.roomType    = roomType;
        this.blockFloor  = blockFloor;
        this.blockCode   = blockCode;
        this.unavailable = unavailable;
    }

    public Integer getRoomNumber()  { return roomNumber; }
    public void setRoomNumber(Integer n) { this.roomNumber = n; }

    public String getRoomType()  { return roomType; }
    public void setRoomType(String t) { this.roomType = t; }

    public Integer getBlockFloor()  { return blockFloor; }
    public void setBlockFloor(Integer f) { this.blockFloor = f; }

    public Integer getBlockCode()  { return blockCode; }
    public void setBlockCode(Integer c) { this.blockCode = c; }

    public Boolean getUnavailable()  { return unavailable; }
    public void setUnavailable(Boolean u) { this.unavailable = u; }

    @Override
    public String toString() {
        return "Room{roomNumber=" + roomNumber + ", roomType=" + roomType
                + ", blockFloor=" + blockFloor + ", blockCode=" + blockCode
                + ", unavailable=" + unavailable + "}";
    }
}
