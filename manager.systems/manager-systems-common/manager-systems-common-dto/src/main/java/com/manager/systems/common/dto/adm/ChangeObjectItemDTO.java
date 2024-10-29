package com.manager.systems.common.dto.adm;

import java.io.Serializable;

import com.manager.systems.common.utils.ConstantDataManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeObjectItemDTO implements Serializable
{
	private static final long serialVersionUID = -3987913989528926680L;
	
	private String valueOld;
	private String valueNew;
	private boolean isChange;
	
	public void verifyChangeValue() {
		if(this.valueOld==null || this.valueNew == null) {
			if(this.valueOld == null && this.valueNew != null) {
				this.valueOld = ConstantDataManager.BLANK;
				this.isChange = true;
			}else if(this.valueOld != null && this.valueNew == null) {
				this.isChange = true;
				this.valueNew = ConstantDataManager.BLANK;
			}else {
				this.valueOld = ConstantDataManager.BLANK;
				this.valueNew = ConstantDataManager.BLANK;
				this.isChange = false;
			}
		}else if(this.valueOld.equalsIgnoreCase(ConstantDataManager.BLANK) || this.valueNew.equalsIgnoreCase(ConstantDataManager.BLANK)) {
			this.valueOld = ConstantDataManager.BLANK;
			this.valueNew = ConstantDataManager.BLANK;
			this.isChange = false;
		}else {
			this.isChange = !(this.valueOld.equalsIgnoreCase(valueNew));
		}
	}
}