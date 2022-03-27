package com.snapchat.streak.Dao;

import com.snapchat.streak.entity.CommonData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommonDataDao extends MongoRepository<CommonData, String> {

    CommonData findByUniqueId(int id);
}
