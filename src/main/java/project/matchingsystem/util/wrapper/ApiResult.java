package project.matchingsystem.util.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

	private T data;

	private String message;

	@Builder
	private ApiResult(T data, String message) {
		this.data = data;
		this.message = message;
	}

	public static <T> ApiResult<T> onSuccess(T data) {
		return ApiResult.<T>builder()
			.data(data)
			.build();
	}

	public static <T> ApiResult<T> onFailure(String message) {
		return ApiResult.<T>builder()
			.message(message)
			.build();
	}
}
