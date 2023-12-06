package at.aau.recipeorganizer.data;

import java.util.List;

public record UserInfoResponse(Long id, String username, List<String> roles) {
}
