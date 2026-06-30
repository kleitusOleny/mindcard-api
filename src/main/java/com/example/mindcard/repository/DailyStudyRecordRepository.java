package com.example.mindcard.repository;

import com.example.mindcard.model.DailyStudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DailyStudyRecordRepository extends JpaRepository<DailyStudyRecord, DailyStudyRecord.DailyStudyRecordId> {
    List<DailyStudyRecord> findByUserId(String userId);
}
