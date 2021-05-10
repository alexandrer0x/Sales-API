package dev.alexandrevieira.sales.domain.enums;

public enum Profile {

	USER(0, "USER"),
	ADMIN(1, "ADMIN");

	private final int code;
	private final String description;
	
	Profile(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCod() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static Profile byCode(Integer code) {
		if (code == null) {
			return null;
		}

		for(Profile x : Profile.values()) {
			if(code.equals(x.getCod())) {
				return x;
			}
		}
		throw new IllegalArgumentException("Invalid code");
	}

	public static Profile byDescription(String description) {
		if (description == null) {
			return null;
		}

		for(Profile x : Profile.values()) {
			if(description.equals(x.getDescription())) {
				return x;
			}
		}
		throw new IllegalArgumentException("Invalid code");
	}
}