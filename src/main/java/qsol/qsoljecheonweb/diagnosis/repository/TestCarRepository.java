package qsol.qsoljecheonweb.diagnosis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qsol.qsoljecheonweb.domain.diagnosis.TestCar;

public interface TestCarRepository extends JpaRepository<TestCar, String> {

}
