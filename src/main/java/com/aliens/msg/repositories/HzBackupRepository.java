package com.aliens.msg.repositories;

import com.aliens.msg.models.HzBackup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jayant on 3/10/16.
 */
public interface HzBackupRepository extends JpaRepository<HzBackup,String> {
}
