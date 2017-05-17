default:
	rm -rf bin	
	mkdir -p bin
	javac -d ./bin src/cs/Cryptography.java src/cs/UserInterface.java
	cd ./bin; java cs/Cryptography
clean:
	rm -rf bin

run:	
	cd ./bin; java cs/Cryptography
