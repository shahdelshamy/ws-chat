'use strict';

const userRegistration = document.querySelector(".user-registration");
const chatContainer = document.querySelector(".chat-container");
const userForm = document.querySelector("#user-form-details");
const chatArea = document.querySelector(".chat-area");
const messageForm = document.querySelector(".message-form");
const logoutButton = document.querySelector(".logout");
const messageInput = document.querySelector(".message-input");

let firstName=null;
let lastName=null;
let phoneNumber=null;
let selectedUserId=null;
let selectedUserFirstName=null;
let selectedUserLastName = null;
let stompClient = null;   //Simple (or Streaming) Text Oriented Messaging Protocol


userForm.addEventListener('submit',connect,true);
messageForm.addEventListener('submit', sendMessage,true);
logoutButton.addEventListener('click',onLogout,true);
window.onbeforeunload=()=> onLogout()

function connect(event) {

    console.log("Connecting to WebSocket");

    firstName = document.querySelector("#first-name").value.trim();
    lastName = document.querySelector("#last-name").value.trim();
    phoneNumber = document.querySelector("#phone-number").value.trim();

    if(firstName && lastName && phoneNumber){

        userRegistration.classList.add("hidden");
        chatContainer.classList.remove("hidden");

        console.log("Connecting to WebSocket 2");

        const socket = new SockJS('http://localhost:9090/ws');
        stompClient = Stomp.over(socket);

        console.log("Connected");
        stompClient.connect({},onConnect,onError);
    }

    event.preventDefault();
}

function onConnect() {
    console.log("Connected to WebSocket");

    //private message subscription
    stompClient.subscribe(`/user/${phoneNumber}/queue/messages`,onMessageReceived);

    //public message subscription
    stompClient.subscribe(`/topic/public`,onMessageReceived);

    //publish user details to the server
    //Blob -> Binary Large Object for sending and store binary data (images, audio, video, object.)
    stompClient.send(
        "/app/user.add",  //destination endpoint
        {},     //headers
        JSON.stringify({    //payload
            firstName:firstName,
            lastName:lastName,
            phoneNumber:phoneNumber
        })
    );

    document.querySelector(".user-name").textContent = `${firstName} ${lastName}`;

    findAndDisplayConnectedUsers().then();
}

  //awaits for the server to send a message when it receives a message the function execute
async function findAndDisplayConnectedUsers(){
    const connectedUserResponse = await fetch('/users');  //Metadata about the connected users
    let connectedUsers = await connectedUserResponse.json();    //Data

    connectedUsers = connectedUsers.filter(user => user.phoneNumber !== phoneNumber); //filter out the current user

    const connectedUsersList = document.querySelector("#online-users-list");
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user=>{

        appendUserElement(user,connectedUsersList);

        const separetor = document.createElement("div");
        separetor.classList.add("separetor");
    });

}

function appendUserElement(user, connectedUsersList) {

    const userItem=document.createElement('li');
    userItem.classList.add('user-item');
    userItem.id = user.phoneNumber;

    const userImage =document.createElement('img');
    userImage.src='../images/userImage.svg';
    userImage.alt = `${user.firstName} ${user.lastName}`;

    const userNameParag = document.createElement('p');
    userNameParag.textContent = `${user.firstName} ${user.lastName}` ;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.classList.add('received-msgs' , 'hidden');
    receivedMsgs.textContent = '0';

    userItem.appendChild(userImage);
    userItem.appendChild(userNameParag);
    userItem.appendChild(receivedMsgs);

    userItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(userItem);
}

function userItemClick(event){

    document.querySelectorAll('.user-item').forEach(
        item=>{
            item.classList.remove('active');
        }
    );

    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.id;
    selectedUserFirstName = clickedUser.querySelector('p').textContent.split(' ')[0];
    selectedUserLastName = clickedUser.querySelector('p').textContent.split(' ')[1];

    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.received-msgs');
    nbrMsg.classList.remove('hidden');
    nbrMsg.textContent = '0'; // Reset the message count when user is clicked

}

async function fetchAndDisplayUserChat(){

    let chatRoomId = `${firstName}_${selectedUserFirstName}`;

    const chatResponse = await  fetch(`/messages/${chatRoomId}`)
    let chatMessages = await chatResponse.json();

    chatArea.innerHTML = '';

    chatMessages.forEach(
      message=>{

          displayChatMessage(message.sender.phoneNumber,message.content);
      }
    );

    chatArea.scrollTop = chatArea.scrollHeight;   //for auto scroll to the bottom of the chat area

}

function displayChatMessage(senderId,content){

    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    if(senderId === phoneNumber){
        messageContainer.classList.add('sender');
    }else{
        messageContainer.classList.add('receiver');
    }

    const messageParag = document.createElement('p');
    messageParag.textContent = content;

    messageContainer.appendChild(messageParag);

    chatArea.appendChild(messageContainer);


}

async function onMessageReceived(payload) {

    await findAndDisplayConnectedUsers();  //for new user registration

    const message = JSON.parse(payload.body);  //convert JSON string to JavaScript object

    if(selectedUserId && selectedUserId === message.sender.phoneNumber){
        displayChatMessage(message.sender.phoneNumber , message.content);

    }

    chatArea.scrollTop = chatArea.scrollHeight;   //for auto scroll to the bottom of the chat area

    if(selectedUserId){
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    }else{
        messageForm.classList.add('hidden');
    }

    const notifidUser = document.querySelector(`#${message.sender.phoneNumber}`);

    if(notifidUser && !notifidUser.classList.contains('active')){
        const receivedMsgs = notifidUser.querySelector('.received-msgs');
        receivedMsgs.classList.remove('hidden');
        receivedMsgs.textContent = '';
    }
}

function sendMessage(event) {

    const messaeContent = messageInput.value.trim();

    if(stompClient && messaeContent){
        let chatMessage ={
            sender:{
                firstName:firstName,
                lastName:lastName,
                phoneNumber:phoneNumber
            },
            receiver:{
                firstName:selectedUserFirstName,
                lastName:selectedUserLastName,
                phoneNumber:selectedUserId
            },
            content:messaeContent
        };

        stompClient.send(
            '/app/chat',  //destination endpoint
            {},     //headers
            JSON.stringify(chatMessage)    //payload
        );

        displayChatMessage(phoneNumber, messaeContent);

        messageInput.value = '';
    }

    chatArea.scrollTop = chatArea.scrollHeight;   //for auto scroll to the bottom of the chat area
    event.preventDefault();
}

function onLogout(){

    stompClient.send(
        '/app/user.disconnected', {},
        JSON.stringify(
            {
                firstName:firstName,
                lastName:lastName,
                phoneNumber:phoneNumber
            }
        )
    );

    window.location.reload();
}

function onError(error) {
    console.log("Error in connecting to WebSocket");
    console.log(error);
}