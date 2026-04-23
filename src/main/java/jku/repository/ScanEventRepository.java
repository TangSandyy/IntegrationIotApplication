package jku.repository;

import jku.entity.ScanEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanEventRepository extends JpaRepository<ScanEvent, Long> {
}