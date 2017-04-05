pragma solidity ^0.4.0;

contract Hashing {
    mapping(bytes32 => uint) public timestamps;
    bytes32  hashCurrent;
    string temp;

    function hash(string message) returns (bytes32) {
        hashCurrent = sha3(message,msg.sender);
        timestamps[hashCurrent] = block.timestamp;
        return hashCurrent;
    }
}

contract Mapping {
    function getTimestamp(bytes32 hash) returns (uint) {
        return Hashing(<address>).timestamps(hash);
    }
}