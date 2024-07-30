import React from 'react';

// 유저일 경우
    // 이웃이 아닐 경우
        // 이웃 신청 Btn
    // 이웃일 경우
        // 
    // 서로이웃일 경우
        // 이웃 취소 btn ()
    // 본인일 경우 : 북마크 아이콘
// 식물일 경우
    // 고정

// 현재, 요청 user와 페이지 user의 관계 (본인, 이웃, 서로이웃, 친구아님)
// 를 ProfileBio에서 확인할지, ProfileHeader에서 확인할지는 미정이기 때문에
// 그 부분을 체크하는 것은 구현하지 않았음 (추후 SNS 추가할 때에 손볼 예정)

const ProfileBio = ({ bio, type }) => {

    const renderUserButtons = () => (
        <div>
            <button>이웃 신청</button>
            <button>서로이웃 신청</button>
            <button>이웃 끊기</button>
        </div>
    );

    const renderPlantIcons = () => (
        <div>
            <i title ="알람"/>
            <i title ="편집"/>
            <i title ="일지"/>
            <i title ="고정"/>
        </div>
    );


    return (
        <div>
            <span>{bio}</span>
            {type === 'user' ? renderUserButtons() : renderPlantIcons()}
        </div>
    );
};

export default ProfileBio;