package org.kiwon.project.entity.member;

import lombok.*;
import org.kiwon.project.entity.BaseEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor //별도의 생성자를 만들었는데 기본 생성자를 만들지 않았을 경우  인텔리j에서 경고줌 -> 생성자 안만들었는데 ㅅㅂ default때문인가?
public class Member extends BaseEntity {

    @Id
    private String email;       //회원 ID

    private String password;    //회원 비번

    private String nickName;    //회원 이름

    private boolean fromSocial; //소셜로그인 사용 여부

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole){
        roleSet.add(memberRole);
    }

    public void changePassword(String password) {this.password = password;}

    public void changeNickName(String nickName) {this.nickName = nickName;}

}
