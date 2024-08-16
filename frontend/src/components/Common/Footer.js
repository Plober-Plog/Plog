import { useNavigate, useLocation } from "react-router-dom";
import { useEffect } from "react";
import useButtonStore from "../../stores/footer";
<<<<<<< HEAD
import snsSelect from "../../assets/icon/footer/sns-select.svg";
import snsDefault from "../../assets/icon/footer/sns-default.svg";
import profileSelect from "../../assets/icon/footer/profile-select.svg";
import profileDefault from "../../assets/icon/footer/profile-default.svg";
import homeSelect from "../../assets/icon/footer/home-select.svg";
import homeDefault from "../../assets/icon/footer/home-default.svg";
import chatSelect from "../../assets/icon/footer/chat-select.svg";
import chatDefault from "../../assets/icon/footer/chat-default.svg";
import alarmSelect from "../../assets/icon/footer/alarm-select.svg";
import alarmDefault from "../../assets/icon/footer/alarm-default.svg";
import alarmNewDefault from "../../assets/icon/footer/alarm-new-default.svg";
import "./Footer.css";
=======
import snsSelect from "../../assets/icon/footer/sns-select.png";
import snsDefault from "../../assets/icon/footer/sns-default.png";
import profileSelect from "../../assets/icon/footer/profile-select.png";
import profileDefault from "../../assets/icon/footer/profile-default.png";
import homeSelect from "../../assets/icon/footer/home-select.png";
import homeDefault from "../../assets/icon/footer/home-default.png";
import chatSelect from "../../assets/icon/footer/chat-select.png";
import chatDefault from "../../assets/icon/footer/chat-default.png";
import alarmSelect from "../../assets/icon/footer/alarm-select.png";
import alarmDefault from "../../assets/icon/footer/alarm-default.png";
import alarmNewDefault from "../../assets/icon/footer/alarm-new-default.png";
import useAuthStore from '../../stores/member';

// import "./Footer.css";
>>>>>>> master

const Footer = () => {
  const { activeButton, setActiveButton } = useButtonStore(); 
  const navigate = useNavigate();
  const location = useLocation();
<<<<<<< HEAD
=======
  const authSearchId = useAuthStore((state) => state.getSearchId());
>>>>>>> master

  // 활성 버튼 바뀔 때마다 해당 icon 활성화
  useEffect(() => {
    const path = location.pathname;
<<<<<<< HEAD
    switch (path) {
      case "/sns":
        setActiveButton("sns");
        break;
      case "/profile":
        setActiveButton("profile");
        break;
      case "/":
        setActiveButton("home");
        break;
      case "/chat":
        setActiveButton("chat");
        break;
      case "/alarm":
        setActiveButton("alarm");
        break;
      default:
        setActiveButton("home");
        break;
=======
    if (path.startsWith("/sns")) {
      setActiveButton("sns");
    } else if (path.startsWith("/profile")) {
      setActiveButton("profile");
    } else if (path.startsWith("/plant")) {
      setActiveButton("profile");
    } else if (path === "/") {
      setActiveButton("home");
    } else if (path.startsWith("/chat")) {
      setActiveButton("chat");
    } else if (path.startsWith("/alarm")) {
      setActiveButton("alarm");
    } else {
      setActiveButton("home");
>>>>>>> master
    }
  }, [location.pathname, setActiveButton]);

  // footer 이동
  const handleClick = (icon) => {
    setActiveButton(icon);

    switch (icon) {
      case "sns":
        navigate("/sns");
        break;
      case "profile":
<<<<<<< HEAD
        navigate("/profile");
=======
        navigate(`/profile/${authSearchId}`);
>>>>>>> master
        break;
      case "home":
        navigate("/");
        break;
      case "chat":
        navigate("/chat");
        break;
      case "alarm":
        navigate("/alarm");
        break;
      default:
        break;
    }
  };

  return (
<<<<<<< HEAD
    <div className="footer-container">
      <button className="footer-button" onClick={() => handleClick("sns")}>
        <img
          src={activeButton === "sns" ? snsSelect : snsDefault}
          alt="sns 아이콘"
          className={activeButton === "sns" ? "footer-icon-select" : "footer-icon"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("profile")}>
        <img
          src={activeButton === "profile" ? profileSelect : profileDefault}
          alt="profile 아이콘"
          className={activeButton === "profile" ? "footer-icon-select" : "footer-icon"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("home")}>
        <img
          src={activeButton === "home" ? homeSelect : homeDefault}
          alt="home 아이콘"
          className={activeButton === "home" ? "footer-icon-select" : "footer-icon"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("chat")}>
        <img
          src={activeButton === "chat" ? chatSelect : chatDefault}
          alt="chat 아이콘"
          className={activeButton === "chat" ? "footer-icon-select" : "footer-icon"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("alarm")}>
        <img
          src={activeButton === "alarm" ? alarmSelect : alarmDefault}
          alt="alarm 아이콘"
          className={activeButton === "alarm" ? "footer-icon-select" : "footer-icon"}
        />
      </button>
    </div>
=======
    <footer className="w-full max-w-custom mx-auto flex justify-between items-center bg-white border-t border-gray-200 fixed bottom-0 left-0 right-0">
      <button className="p-4" onClick={() => handleClick("sns")}>
        <img
          src={activeButton === "sns" ? snsSelect : snsDefault}
          alt="SNS"
          className="w-6 h-6"
        />
      </button>
      <button className="p-4" onClick={() => handleClick("profile")}>
        <img
          src={activeButton === "profile" ? profileSelect : profileDefault}
          alt="프로필"
          className="w-6 h-6"
        />
      </button>
      <button className="p-4" onClick={() => handleClick("home")}>
        <img
          src={activeButton === "home" ? homeSelect : homeDefault}
          alt="홈"
          className="w-6 h-6"
        />
      </button>
      <button className="p-4" onClick={() => handleClick("chat")}>
        <img
          src={activeButton === "chat" ? chatSelect : chatDefault}
          alt="채팅"
          className="w-6 h-6"
        />
      </button>
      <button className="p-4" onClick={() => handleClick("alarm")}>
        <img
          src={activeButton === "alarm" ? alarmSelect : alarmDefault}
          alt="알람"
          className="w-6 h-6"
        />
      </button>
    </footer>
>>>>>>> master
  );
};

export default Footer;
