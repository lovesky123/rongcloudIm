package com.ycjt.rongcloudim.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ycjt.rongcloudim.db.model.BlackListEntity;
import com.ycjt.rongcloudim.db.model.FriendInfo;
import com.ycjt.rongcloudim.db.model.FriendShipInfo;
import com.ycjt.rongcloudim.model.UserSimpleInfo;

import java.util.List;

@Dao
public interface FriendDao {
    @Query("SELECT friend.id as id ,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend " +
            "left join user " +
            "on friend.id = user.id " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> getAllFriendListDB();

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id = :id")
    LiveData<FriendShipInfo> getFriendInfo(String id);

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id = :id")
    FriendShipInfo getFriendInfoSync(String id);

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id in (:ids)")
    List<FriendShipInfo> getFriendInfoListSync(String[] ids);

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id in (:ids)")
    LiveData<List<FriendShipInfo>> getFriendInfoList(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFriendShip(FriendInfo friendInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFriendShipList(List<FriendInfo> friendInfoList);

    @Query("SELECT friend.id as id ,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend " +
            "left join user " +
            "on friend.id = user.id " +
            "where user.name like '%' || :matchSearch || '%'" +
            "OR user.alias like '%' || :matchSearch || '%' " +
            "OR user.name_spelling like '%$' || :matchSearch || '%' " +
            "OR user.alias_spelling like '%$' || :matchSearch || '%' " +
            "OR user.name_spelling_initial  like '%' || :matchSearch || '%' " +
            "OR user.alias_spelling_initial  like '%' || :matchSearch || '%' " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> searchFriendShip(String matchSearch);

    @Query("SELECT user.id,user.name,user.portrait_uri FROM black_list INNER JOIN user ON black_list.id = user.id WHERE black_list.id=:userId")
    LiveData<UserSimpleInfo> getUserInBlackList(String userId);

    @Query("SELECT user.id,user.name,user.portrait_uri FROM black_list INNER JOIN user ON black_list.id = user.id")
    LiveData<List<UserSimpleInfo>> getBlackListUser();

    @Query("DELETE FROM black_list")
    void deleteAllBlackList();

    @Query("DELETE FROM black_list WHERE id=:id")
    void removeFromBlackList(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToBlackList(BlackListEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateBlackList(List<BlackListEntity> blackListEntityList);

    @Query("SELECT friend.id as id ,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend " +
            "left join user " +
            "on friend.id = user.id " +
            "where friend.id " +
            "not in (select DISTINCT(group_member.user_id) from group_member where group_member.group_id =:excluedGroupId) " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> getAllFriendsExcluedGroup(String excluedGroupId);

    @Query("SELECT group_member.user_id as id ,alias,portrait_uri,name,region,phone_number,friend_status,alias_spelling, name_spelling,order_spelling,message, updateAt " +
            "FROM group_member " +
            "left join user " +
            "on group_member.user_id = user.id " +
            "left join friend " +
            "on group_member.user_id = friend.id " +
            "where group_member.group_id =:includeGroupId " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> getFriendsIncludeGroup(String includeGroupId);

    @Query("DELETE FROM friend WHERE id=:friendId")
    void deleteFriend(String friendId);
}
