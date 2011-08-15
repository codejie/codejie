#ifndef __COMMANDPACKET_H__
#define __COMMANDPACKET_H__

#include <iostream>
#include <string>
#include <sstream>

class CommandPacket
{
public:
    enum CommandType { CT_POST = 0, CT_ACK };

    static const std::string CMD_POST;
    static const std::string CMD_ACK;

    static const std::string CMD_TAG_MN;
    static const std::string CMD_TAG_MFLAG;
    static const std::string CMD_TAG_QN;
    static const std::string CMD_TAG_QNRTN;

public:
    CommandPacket(CommandType type)
        : _type(type)
    {
    }
    virtual ~CommandPacket() {}

    static int Analyse(const std::string& stream, CommandPacket*& packet);

    int Make(std::string& stream) const;

    virtual void Show(std::ostream& os) const;
protected:
    virtual int Analyse(const std::string& stream) { return -1; }
    virtual int Make(std::ostringstream& ostr) const { return -1; }
public:
    CommandType _type;
};


class CmdPostPacket : public CommandPacket
{
public:
    CmdPostPacket();
    virtual ~CmdPostPacket() {}

    virtual void Show(std::ostream& os) const;
protected:
    virtual int Analyse(const std::string& data);
public:
    std::string MN;
    std::string MFlag;
    std::string QN;
};

class CmdAckPacket : public CommandPacket
{
public:
    CmdAckPacket();
    virtual ~CmdAckPacket() {}

    virtual void Show(std::ostream& os) const;
protected:
    virtual int Make(std::ostringstream& ostr) const;

public:
    std::string QnRtn;

};

extern std::ostream& operator << (std::ostream& os, const CommandPacket& packet);


#endif
