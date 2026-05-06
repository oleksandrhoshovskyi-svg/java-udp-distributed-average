# Java UDP Distributed Average

Java console application that demonstrates UDP-based communication using a simple master-slave model.

The first instance that successfully opens the selected port becomes the master. Other instances act as slaves and send numbers to the master through UDP datagrams. The master collects the received numbers, calculates the average when it receives `0`, and shuts down when it receives `-1`.

## Main Features

- UDP communication using `DatagramSocket`
- Master-slave behavior based on port availability
- Sending numbers between separate program instances
- Average calculation from received values
- Broadcast message support
- Basic command-line argument validation
- Simple distributed systems practice in Java

## Technologies Used

- Java
- UDP sockets
- IntelliJ IDEA
- Git / GitHub

## Project Structure

```text
java-udp-distributed-average/
├── src/
│   └── DistributedAverageSystem.java
├── .gitignore
└── README.md
```

## How It Works

The program is started with two command-line arguments:

```text
java DistributedAverageSystem <port> <number>
```

The first argument is the UDP port.  
The second argument is the number sent to the system.

If the program can bind to the selected port, it starts in master mode. If the port is already in use, it starts in slave mode and sends its number to the master.

## Master Mode

The master starts when no other instance is using the selected port.

Example:

```text
java DistributedAverageSystem 5000 10
```

This starts the master on port `5000` and adds the initial number `10`.

The master then waits for incoming UDP messages.

## Slave Mode

If the port is already occupied by the master, a new instance becomes a slave.

Example:

```text
java DistributedAverageSystem 5000 20
```

This sends the number `20` to the master.

## Special Values

| Value | Meaning |
|---:|---|
| `0` | Calculate and print the average of collected numbers |
| `-1` | Broadcast shutdown message and stop the master |
| Any other integer | Add the number to the collected values |

## Example Usage

Start the master:

```text
java DistributedAverageSystem 5000 10
```

Send more numbers from other terminal windows:

```text
java DistributedAverageSystem 5000 20
java DistributedAverageSystem 5000 30
```

Calculate the average:

```text
java DistributedAverageSystem 5000 0
```

Stop the system:

```text
java DistributedAverageSystem 5000 -1
```

## Notes

This project was created as a Java networking practice project.

The main goal is to demonstrate basic UDP communication, command-line argument handling, master-slave behavior, and simple distributed calculation logic.
