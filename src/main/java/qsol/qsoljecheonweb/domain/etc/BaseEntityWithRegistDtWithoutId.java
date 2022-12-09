package qsol.qsoljecheonweb.domain.etc;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import qsol.qsoljecheonweb.util.QsolManagerInfoUtil;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

/**
 * 생성자 아이디가 없는
 * 생성일시, 컬럼을 가지는 수퍼 클래스
 * 
 * @author admin
 *
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntityWithRegistDtWithoutId {

	@CreationTimestamp
	@Column(name="regist_dt", updatable=false)
	private LocalDateTime regist_dt;

}
