package qsol.qsoljecheonweb.domain.etc;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import qsol.qsoljecheonweb.util.QsolManagerInfoUtil;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


/**
 * 생성자 아이디가 없는
 * 생성일시, 수정일시, 수정자 아이디 컬럼을 가지는 수퍼 클래스
 * 
 * @author admin
 *
 */

@MappedSuperclass
@Getter
@Setter
public class BaseEntityWithUpdatedWithoutId extends BaseEntityWithCreatedWithoutId {

	@Column(name="updated_by", insertable=false)
	private String updated_by;
	
	@UpdateTimestamp
	@Column(name="updated_at", insertable=false)
	private LocalDateTime updated_at;

	@PreUpdate
	private void onPreUpdate() {

		try {
			this.updated_by = QsolManagerInfoUtil.getManagerId();
		} catch (Exception e) {
			this.updated_by = "SYSTEM";
		}
	}
}
