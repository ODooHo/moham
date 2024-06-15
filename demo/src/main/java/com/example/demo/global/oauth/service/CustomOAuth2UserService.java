package com.example.demo.global.oauth.service;

import com.example.demo.api.user.UserRole;
import com.example.demo.api.user.dto.UserDto;
import com.example.demo.api.user.entity.UserEntity;
import com.example.demo.api.user.repository.UserRepository;
import com.example.demo.global.oauth.CustomOAuth2User;
import com.example.demo.global.oauth.response.KakaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        /**
         * 구글을 추가할 경우 처리 필요
         */

        KakaoResponse kakaoResponse = new KakaoResponse(oAuth2User.getAttributes());


        UserEntity userEntity = userRepository.findByUsername(kakaoResponse.getName());

        if(userEntity == null){
            UserEntity user = UserEntity.of(
                    kakaoResponse.getName(),
                    kakaoResponse.getEmail(),
                    UserRole.USER
            );
            userRepository.save(user);

            return new CustomOAuth2User(UserDto.from(user));

        } else{
            return new CustomOAuth2User(UserDto.from(userEntity));
        }

    }
}