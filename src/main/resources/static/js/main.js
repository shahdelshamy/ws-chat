'use strict';

const userRegistration = document.querySelector(".user-registration");
const chatContainer = document.querySelector(".chat-container");
const userForm = document.querySelector("#user-form-details");
const chatArea = document.querySelector(".chat-area");
const chatMessages = document.querySelector(".chat-messages");
const messageForm = document.querySelector(".message-form");
const logoutButton = document.querySelector(".logout");
const messageInput = document.querySelector(".message-input");

let userFirstName=null;
let userLastName=null;
let userPhoneNumber=null;
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

    userFirstName = document.querySelector("#first-name").value.trim();
    userLastName = document.querySelector("#last-name").value.trim();
    userPhoneNumber = document.querySelector("#phone-number").value.trim();

    if(userFirstName && userLastName && userPhoneNumber){

        userRegistration.classList.add("hidden");
        chatContainer.classList.remove("hidden");

        console.log("Connecting to WebSocket 2");

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        console.log("Connected");
        stompClient.connect({phoneNumber: userPhoneNumber },
            onConnect,onError);
    }

    event.preventDefault();
}

function onConnect() {
    console.log("Connected to WebSocket");

    //public message subscription
    stompClient.subscribe(`/topic/public`, onMessageReceived);

    //private message subscription
    // stompClient.subscribe(`/user/${userPhoneNumber}/queue/messages`, onMessageReceived);

    stompClient.subscribe("/user/queue/messages", onMessageReceived);

    //publish user details to the server
    //Blob -> Binary Large Object for sending and store binary data (images, audio, video, object.)
    stompClient.send(
        "/app/user.add",  //destination endpoint
        {},     //headers
        JSON.stringify({    //payload
            firstName: userFirstName,
            lastName: userLastName,
            phoneNumber: userPhoneNumber
        })
    );

    document.querySelector(".user-name").textContent = `${userFirstName} ${userLastName}`;

    findAndDisplayConnectedUsers().then();
}

//awaits for the server to send a message when it receives a message the function execute
async function findAndDisplayConnectedUsers(){
    const connectedUserResponse = await fetch('/users');  //Metadata about the connected users
    let connectedUsers = await connectedUserResponse.json();    //Data

    connectedUsers = connectedUsers.filter(user => user.phoneNumber !== userPhoneNumber); //filter out the current user

    const connectedUsersList = document.querySelector("#online-users-list");
    connectedUsersList.innerHTML = '';

    if(!chatContainer.classList.contains('hidden')){
        chatArea.classList.add('hidden');
    }

    for (const user of connectedUsers) {
        const lastMessage = await getLastUserMessage(user.phoneNumber) || { content: "", date: "" };
        appendUserElement(lastMessage, user, connectedUsersList);
    }

}

async function getLastUserMessage(recipientPhoneNumber) {
    const lastUserMessageResponse = await fetch(`/lastMessage/${userPhoneNumber}/${recipientPhoneNumber}`);  //Metadata about the connected users
    // let lastUserMessage = await lastUserMessageResponse.json();
    // if (lastUserMessage) {
    //     return lastUserMessage;
    // }
    // else{
    //     return null;
    // }

    const text = await lastUserMessageResponse.text();
    if (!text.trim()) {
        return null;
    }

    return JSON.parse(text);

}

function appendUserElement(lastUserMessage,user, connectedUsersList) {

    const userItem=document.createElement('li');
    userItem.classList.add('user-item' , 'separetor');
    userItem.id = `user-${user.phoneNumber}`;

    const userImage =document.createElement('img');
    userImage.src='../images/userImage.jpg';
    userImage.alt = `${user.firstName} ${user.lastName}`;

    const userInfo = document.createElement('div');
    userInfo.classList.add('user-info');

    const nameAndReceiveMsg = document.createElement('div');
    nameAndReceiveMsg.classList.add('name-and-receive-msg');

    const userNameParag = document.createElement('p');
    userNameParag.textContent = `${user.firstName} ${user.lastName}` ;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.classList.add('received-msgs' , 'hidden');
    receivedMsgs.textContent = '0';

    const messageAndDate = document.createElement('div');
    messageAndDate.classList.add('message-and-date');

    const lastMsgParag = document.createElement('p');
    lastMsgParag.textContent = lastUserMessage.content || '';
    lastMsgParag.style.color = '#ddd';
    lastMsgParag.style.width = '60px';
    lastMsgParag.style.margin = '1px 25px 1px 0';
    lastMsgParag.style.whiteSpace = 'nowrap';
    lastMsgParag.style.overflow = 'hidden';
    lastMsgParag.style.textOverflow = 'ellipsis';

    const lastMsgDate = document.createElement('p');
    lastMsgDate.textContent = lastUserMessage.date || '';
    lastMsgDate.style.fontSize = '0.75rem';
    lastMsgDate.style.color = '#c5baba';
    lastMsgDate.style.margin = '1px 0';


    nameAndReceiveMsg.appendChild(userNameParag);
    nameAndReceiveMsg.appendChild(receivedMsgs);

    messageAndDate.appendChild(lastMsgParag);
    messageAndDate.appendChild(lastMsgDate);

    userInfo.appendChild(nameAndReceiveMsg);
    userInfo.appendChild(messageAndDate);


    userItem.appendChild(userImage);
    userItem.appendChild(userInfo);

    userItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(userItem);
}

function userItemClick(event){

    document.querySelectorAll('.user-item').forEach(
        item=>{
            item.classList.remove('active');
        }
    );

    chatArea.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.id.split('-')[1];
    selectedUserFirstName = clickedUser.querySelector('p').textContent.split(' ')[0];
    selectedUserLastName = clickedUser.querySelector('p').textContent.split(' ')[1];

    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.user-info .name-and-receive-msg .received-msgs');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = ''; // Reset the message count when user is clicked

}

async function fetchAndDisplayUserChat(){

    // let chatRoomId = `${userPhoneNumber}_${selectedUserId}`;

    const chatResponse = await  fetch(`/messages/${userPhoneNumber}/${selectedUserId}`);
    let chat = await chatResponse.json();

    console.log("chat messages:", chat);

    chatMessages.innerHTML = '';

    chat.forEach(
        message=>{

            displayChatMessage(message.sender.phoneNumber,message.content);
        }
    );

    chatArea.scrollTop = chatArea.scrollHeight;   //for auto scroll to the bottom of the chat area

}

function displayChatMessage(senderId,content){

    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    if(senderId === userPhoneNumber){
        messageContainer.classList.add('sender');
    }else{
        messageContainer.classList.add('receiver');
    }

    const messageParag = document.createElement('p');
    messageParag.textContent = content;

    messageContainer.appendChild(messageParag);

    chatMessages.appendChild(messageContainer);

}

async function onMessageReceived(payload) {

    const message = JSON.parse(payload.body);

    await findAndDisplayConnectedUsers();

    // const lastMessage = {
    //     content: message.content,
    //     date: new Date().toISOString().slice(0, 19).replace('T', ' ') // 2025-08-06 17:57:40
    // };

    updateLastMessage(message.sender.phoneNumber, message);

    if (selectedUserId && selectedUserId === message.sender.phoneNumber) {
        chatArea.classList.remove('hidden');
        displayChatMessage(message.sender.phoneNumber, message.content);
    }

    chatArea.scrollTop = chatArea.scrollHeight;


    if (selectedUserId) {
        document.querySelector(`#user-${selectedUserId}`)?.classList.add('active');
    } else {
        chatArea.classList.add('hidden');
    }

    const notifidUser = document.querySelector(`#user-${message.sender.phoneNumber}`);

    if (notifidUser) {
        const receivedMsgs = notifidUser.querySelector('.user-info .name-and-receive-msg .received-msgs');

        if (!notifidUser.classList.contains('active')) {
            receivedMsgs.classList.remove('hidden');
            // let count = parseInt(receivedMsgs.textContent) || 0;
            receivedMsgs.textContent = '';
        } else {
            receivedMsgs.textContent = '';
            receivedMsgs.classList.add('hidden');
        }
    }
}

function updateLastMessage(userPhoneNumberToUpdate, lastMessage) {
    const userElement = document.querySelector(`#user-${userPhoneNumberToUpdate}`);
    if (!userElement) return;

    // Update the last message text
    const lastMsgParag = userElement.querySelector('.message-and-date p:first-child');
    if (lastMsgParag) {
        lastMsgParag.textContent = lastMessage?.content || '';
    }

    // Update the last message date
    const lastMsgDate = userElement.querySelector('.message-and-date p:last-child');
    if (lastMsgDate) {
        lastMsgDate.textContent = lastMessage?.date || '';
    }
}


async function sendMessage(event) {

    event.preventDefault();

    const messageContent = messageInput.value.trim();

    if(stompClient && messageContent){
        let chatMessage ={
            sender:{
                firstName:userFirstName,
                lastName:userLastName,
                phoneNumber:userPhoneNumber
            },
            recipient:{
                firstName:selectedUserFirstName,
                lastName:selectedUserLastName,
                phoneNumber:selectedUserId
            },
            content:messageContent,
            date: new Date().toISOString().slice(0, 19)
        };

        stompClient.send(
            '/app/chat',  //destination endpoint
            {},     //headers
            JSON.stringify(chatMessage)    //payload
        );

        displayChatMessage(userPhoneNumber, messageContent);

        updateLastMessage(selectedUserId , chatMessage);

        messageInput.value = '';
    }

    chatArea.scrollTop = chatArea.scrollHeight;   //for auto scroll to the bottom of the chat are
}


function onLogout(){

    stompClient.send(
        '/app/user.disconnected', {},
        JSON.stringify(
            {
                firstName:userFirstName,
                lastName:userLastName,
                phoneNumber:userPhoneNumber
            }
        )
    );

    window.location.reload();
}

function onError(error) {
    console.log("Error in connecting to WebSocket");
    console.log(error);
}