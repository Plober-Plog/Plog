import React from'react';

const ProfileImgAndTitle = ({ imgSrc, title, nickname }) => {
    return (
        <div>
            <div>
                <img src={imgSrc} alt="profile img"/>
            </div>
            <div>
                <span>
                    {title}
                </span>
            </div>
            <div>
                <span>
                    {nickname}
                </span>
            </div>
        </div>
    );
};

export default ProfileImgAndTitle;